package me.obsilabor.torch.gradle.cli

import java.io.File

object GradleCLI {

    fun checkInstallation(): Boolean {
        runCatching {
            executeGradleCmd(true, null,"--help")
        }.onFailure {
            it.printStackTrace()
            return false
        }
        return true
    }

    fun wrapper(dir: File) {
        executeGradleCmd(false, dir,"-q", "wrapper")
    }

    private fun executeGradleCmd(quiet: Boolean, dir: File?, vararg cmd: String) {
        val gradle = if (System.getProperty("os.name").lowercase().contains("windows")) "gradle.cmd" else "gradle"
        val command = mutableListOf(gradle)
        command.addAll(cmd.toList())
        val builder = ProcessBuilder(command).directory(dir ?: File(System.getProperty("user.dir")))
        if (!quiet) {
            builder.inheritIO()
        } else {
            builder.redirectInput(ProcessBuilder.Redirect.INHERIT).redirectOutput(ProcessBuilder.Redirect.DISCARD)
        }
        builder.start().waitFor()
    }

}