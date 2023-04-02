package me.obsilabor.torch.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.mordant.rendering.TextStyles

class TorchCommand : CliktCommand(
    name = "torch",
    help = """
       torch is a command line tool to easily create minecraft kotlin projects.
        
        ${(TextStyles.bold + TextStyles.underline)("Resources")}${TextStyles.bold(":")}
        ${TextStyles.hyperlink("https://github.com/mooziii/torch")("Source Code")}
        ${TextStyles.hyperlink("https://github.com/mooziii/torch/issues")("Issues")}
        ${TextStyles.hyperlink("https://github.com/mooziii/torch/issues/new")("Report a bug")}
    """
) {
    init {
        subcommands(
            LitCommand()
        )
    }

    override fun run() = Unit
}