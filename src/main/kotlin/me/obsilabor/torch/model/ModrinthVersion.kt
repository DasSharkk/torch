package me.obsilabor.torch.model

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class ModrinthVersion(
    val version_number: String,
    val game_versions: List<String>
)