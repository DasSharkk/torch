package me.obsilabor.torch.commands

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import me.obsilabor.pistonmetakt.PistonMetaClient
import me.obsilabor.torch.api.GradlePluginResolver
import me.obsilabor.torch.gradle.ConfigurationType
import me.obsilabor.torch.gradle.DependencyConfiguration
import me.obsilabor.torch.gradle.TaskConfiguration
import me.obsilabor.torch.gradle.cli.GradleCLI
import me.obsilabor.torch.gradle.generateBuildScript
import me.obsilabor.torch.httpClient
import me.obsilabor.torch.mainScope
import me.obsilabor.torch.model.FabricLoaderVersion
import me.obsilabor.torch.model.ModrinthVersion
import me.obsilabor.torch.terminal
import me.obsilabor.torch.toolchain.Mappings
import java.io.File

class LitCommand : CliktCommand(
    name = "lit",
    help = "Creates a new kotlin project"
) {
    private val projectName by argument(
        "projectName",
        help = "The name of the kotlin project to generate"
    )

    private val shadowFlag by option(
        "-S", "--shadow",
        help = "Adds the shadow gradle plugin to this project"
    ).flag()

    private val javaVersion by option(
        "-j", "--java",
        help = "Specifies the java version"
    ).default("17")

    private val fabricFlag by option(
        "-f", "--fabric",
        help = "Adds the fabric modding toolchain to this project"
    ).flag()

    private val paperFlag by option(
        "-p", "--paper",
        help = "Adds the paper modding toolchain to this project"
    ).flag()

    private val mappings by option(
        "-m", "--mappings",
        help = "Specifies which mappings to use for this project",
        completionCandidates = CompletionCandidates.Fixed("mojang", "yarn")
    ).default("mojang")

    private val mcVersion by option(
        "-v", "--version", "-M", "--minecraft-version",
        help = "Specifies which minecraft version to use for this project",
    )

    private val GRADLE_RESOLVER = GradlePluginResolver()
    private val PAPER_RESOLVER = GradlePluginResolver("https://repo.papermc.io/repository/maven-public/", true)
    private val FABRIC_RESOLVER = GradlePluginResolver("https://maven.fabricmc.net/", true)

    override fun run() {
        mainScope.launch {
            if (!GradleCLI.checkInstallation()) {
                terminal.println(TextColors.red("Gradle is not installed. Please ${TextStyles.hyperlink("https://gradle.org/install/")("install gradle")} first!"))
                return@launch
            }
            val projectDirectory = File(System.getProperty("user.dir"), projectName)
            if (projectDirectory.exists()) {
                terminal.println(TextColors.red("Cannot use directory $projectName: Directory already exists!"))
                return@launch
            }
            projectDirectory.mkdir()
            val buildGradleKts = createFile(File(projectDirectory, "build.gradle.kts"))
            val settingsGradleKts = createFile(File(projectDirectory, "settings.gradle.kts"))
            val buildScript = generateBuildScript {
                artifactId = projectName
                if (shadowFlag) {
                    addGradlePlugin(
                        "com.github.johnrengelman.shadow",
                        GRADLE_RESOLVER.resolveVersion(
                            "com.github.johnrengelman.shadow",
                            "gradle.plugin"
                        )
                    )
                    terminal.println(TextColors.brightBlue("* shadow plugin installed!"))
                }
                if (fabricFlag) {
                    addGradlePlugin("fabric-loom", FABRIC_RESOLVER.resolveVersion("net.fabricmc", "fabric-loom"))
                    addPluginRepository("https://maven.fabricmc.net/")
                    terminal.println(TextColors.brightBlue("* fabric-loom plugin installed!"))
                    val mcVersion = mcVersion ?: PistonMetaClient.getLauncherMeta().latest.release
                    addDependency(DependencyConfiguration.MINECRAFT, "\"com.mojang:minecraft:$mcVersion\"")
                    val mappings = Mappings.valueOf(mappings.uppercase())
                    mappings.additionalMavenRepos.forEach { addMavenRepository(it) }
                    mappings.additionalGradleRepos.forEach { addPluginRepository(it) }
                    addDependency(DependencyConfiguration.MAPPINGS, mappings.appendToScript(mcVersion))
                    addDependency(
                        DependencyConfiguration.MOD_IMPLEMENTATION,
                        "\"${httpClient.get("https://meta.fabricmc.net/v2/versions/loader")
                            .body<List<FabricLoaderVersion>>().first { it.stable }.maven}\""
                    )
                    addDependency(
                        DependencyConfiguration.MOD_IMPLEMENTATION,
                        "\"net.fabricmc.fabric-api:fabric-api:${httpClient.get("https://api.modrinth.com/v2/project/fabric-api/version")
                            .body<List<ModrinthVersion>>().first { it.game_versions.contains(mcVersion) }.version_number}\""
                    )
                    addDependency(
                        DependencyConfiguration.MOD_IMPLEMENTATION,
                        "\"net.fabricmc:fabric-language-kotlin:${httpClient.get("https://api.modrinth.com/v2/project/fabric-language-kotlin/version")
                            .body<List<ModrinthVersion>>().first().version_number}\""
                    )
                    terminal.println(TextColors.brightBlue("* Fabric dependencies installed!"))
                }
                if (paperFlag) {
                    addGradlePlugin("io.papermc.paperweight.userdev", PAPER_RESOLVER.resolveVersion("io.papermc.paperweight", "paperweight-userdev"))
                    if (!fabricFlag) {
                        addGradlePlugin("xyz.jpenilla.run-paper", GRADLE_RESOLVER.resolveVersion("xyz.jpenilla", "run-paper"))
                    }
                    addPluginRepository("https://repo.papermc.io/repository/maven-public/")
                    terminal.println(TextColors.brightBlue("* userdev plugin installed!"))
                    val mcVersion = mcVersion ?: PistonMetaClient.getLauncherMeta().latest.release
                    addDependency(DependencyConfiguration.PAPER_DEV_BUNDLE, "\"$mcVersion-R0.1-SNAPSHOT\"")
                    terminal.println(TextColors.brightBlue("* Paper dependencies installed!"))
                }
                addKotlinConfiguration(TaskConfiguration(ConfigurationType.INVOKE, "jvmToolchain", javaVersion))
            }
            buildScript.writeToFiles(buildGradleKts.toPath(), settingsGradleKts.toPath())
            createFile(File(projectDirectory, "src/main"), true)
            createFile(File(projectDirectory, "src/main/kotlin"), true)
            createFile(File(projectDirectory, "src/main/resources"), true)
            createFile(File(projectDirectory, "src/test"), true)
            createFile(File(projectDirectory, "src/test/kotlin"), true)
            createFile(File(projectDirectory, "src/test/resources"), true)
            createFile(File(projectDirectory, "gradle.properties")).writeText("org.gradle.jvmargs=-Xmx2G")
            terminal.println(TextColors.brightCyan("* Project created, installing gradle wrapper..."))
            GradleCLI.wrapper(projectDirectory)
            terminal.println(TextColors.brightGreen("* Project setup complete!"))
        }
    }

    private fun createFile(file: File, dir: Boolean = false): File {
        if (dir) file.mkdirs() else file.createNewFile()
        return file
    }
}