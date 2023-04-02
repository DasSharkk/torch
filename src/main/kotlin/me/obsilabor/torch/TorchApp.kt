package me.obsilabor.torch

import kotlinx.coroutines.coroutineScope
import me.obsilabor.torch.commands.TorchCommand

suspend fun main(args: Array<String>) {
    coroutineScope {
        mainScope = this
        TorchCommand().main(args)
    }
}