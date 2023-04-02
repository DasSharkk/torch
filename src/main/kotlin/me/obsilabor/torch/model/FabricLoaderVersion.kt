package me.obsilabor.torch.model

import kotlinx.serialization.Serializable

@Serializable
data class FabricLoaderVersion(
    val stable: Boolean,
    val maven: String
)
