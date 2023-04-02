package me.obsilabor.torch.gradle

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import me.obsilabor.torch.api.MavenCentralResolver
import me.obsilabor.torch.httpClient
import me.obsilabor.torch.model.MavenMetadata
import me.obsilabor.torch.tab
import java.nio.file.Path
import kotlin.io.path.writeText

@Suppress("MemberVisibilityCanBePrivate")
class GradleBuildScript(
    var groupId: String,
    var artifactId: String,
    val mavenRepositories: List<String>,
    val pluginRepositories: List<String>,
    val dependencies: List<Dependency>,
    val gradlePlugins: List<GradlePlugin>,
    val taskConfigurations: Map<String, List<TaskConfiguration>>,
    val kotlinConfigurations: List<TaskConfiguration>
) {
    suspend fun writeToFiles(buildGradleKts: Path, settingsGradleKts: Path) {
        buildGradleKts.writeText(buildString {
            appendLine("plugins {")
            appendLine("${tab()}kotlin(\"jvm\") version \"${MavenCentralResolver.resolveVersion("org.jetbrains.kotlin", "kotlin-stdlib")}\"")
            for (gradlePlugin in gradlePlugins) {
                appendLine("${tab()}id(\"${gradlePlugin.name}\")${if (gradlePlugin.version != null) " version \"${gradlePlugin.version}\"" else ""}")
            }
            appendLine("}")
            appendLine()
            appendLine("group = \"${groupId}\"")
            appendLine("version = \"1.0.0\"")
            appendLine()
            appendLine("repositories {")
            appendLine("${tab()}mavenCentral()")
            for (mavenRepo in mavenRepositories) {
                appendLine("${tab()}maven(\"$mavenRepo\")")
            }
            appendLine("}")
            appendLine()
            appendLine("dependencies {")
            appendLine("${tab()}implementation(kotlin(\"stdlib\"))")
            for (dependency in dependencies) {
                appendLine("${tab()}${dependency.configuration.function}(${dependency.dependencyNotation})")
            }
            appendLine("}")
            if (taskConfigurations.isNotEmpty()) {
                appendLine()
                appendLine("tasks {")
                taskConfigurations.forEach {
                    appendLine("${tab()}${it.key} {")
                    it.value.forEach { config ->
                        appendLine("${tab()}${tab()}${config.key}${if (config.type == ConfigurationType.INVOKE)"(${config.value})" else " = ${config.value}"}")
                    }
                    appendLine("${tab()}}")
                }
                appendLine("}")
            }
            if (kotlinConfigurations.isNotEmpty()) {
                appendLine()
                appendLine("kotlin {")
                kotlinConfigurations.forEach {
                    appendLine("${tab()}${it.key}${if (it.type == ConfigurationType.INVOKE)"(${it.value})" else " = ${it.value}"}")
                }
                appendLine("}")
            }
        })
        settingsGradleKts.writeText(buildString {
            appendLine("rootProject.name = \"$artifactId\"")
            if (pluginRepositories.isNotEmpty()) {
                appendLine()
                appendLine("pluginManagement {")
                appendLine("${tab()}repositories {")
                appendLine("${tab()}${tab()}gradlePluginPortal()")
                pluginRepositories.forEach {
                    appendLine("${tab()}${tab()}maven(\"${it}\")")
                }
                appendLine("${tab()}}")
                appendLine("}")
            }
        })
    }
}