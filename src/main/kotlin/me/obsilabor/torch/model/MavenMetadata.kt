package me.obsilabor.torch.model

import kotlinx.serialization.Serializable

@Serializable
data class MavenMetadata(
    val response: MavenSearchResponse
)

@Serializable
data class MavenSearchResponse(
    val docs: List<MavenDocument>
)

@Serializable
data class MavenDocument(
    val id: String,
    val latestVersion: String
)