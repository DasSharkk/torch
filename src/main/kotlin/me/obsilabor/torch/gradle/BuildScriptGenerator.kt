package me.obsilabor.torch.gradle

import me.obsilabor.torch.toolchain.Mappings

inline fun generateBuildScript(
    builder: BuildScriptBuilder.() -> Unit
) = BuildScriptBuilder().apply(builder).build()

class BuildScriptBuilder {

    var groupId: String = "com.example"
    var artifactId: String? = null
    val mavenRepositories = mutableSetOf<String>()
    val pluginRepositories = mutableSetOf<String>()
    val dependencies = mutableSetOf<Dependency>()
    val plugins = mutableSetOf<GradlePlugin>()
    val taskConfigurations = mutableMapOf<String, ArrayList<TaskConfiguration>>()
    val kotlinConfigurations = mutableSetOf<TaskConfiguration>()

    fun addMavenRepository(url: String) {
        mavenRepositories += url
    }

    fun addPluginRepository(url: String) {
        pluginRepositories += url
    }

    fun addDependency(configuration: DependencyConfiguration, notation: String) {
        dependencies += Dependency(configuration, notation)
    }

    fun addGradlePlugin(id: String, version: String? = null) {
        plugins += GradlePlugin(id, version)
    }

    fun addTaskConfiguration(task: String, configuration: TaskConfiguration) {
        if (taskConfigurations[task] == null) {
            taskConfigurations[task] = arrayListOf(configuration)
            return
        }
        (taskConfigurations[task] ?: return) += configuration
    }

    fun addKotlinConfiguration(configuration: TaskConfiguration) {
        kotlinConfigurations += configuration
    }

    fun build(): GradleBuildScript {
        return GradleBuildScript(groupId, artifactId ?: throw RuntimeException("No artifactId specified!"), mavenRepositories.toList(), pluginRepositories.toList(), dependencies.toList(), plugins.toList(), taskConfigurations, kotlinConfigurations.toList())
    }
}