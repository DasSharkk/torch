package me.obsilabor.torch.toolchain

import io.ktor.client.call.*
import io.ktor.client.request.*
import me.obsilabor.torch.httpClient
import me.obsilabor.torch.model.YarnVersion

enum class Mappings(val additionalMavenRepos: List<String>, val additionalGradleRepos: List<String>, val appendToScript: suspend (mcVersion: String) -> String) {
    MOJANG(listOf(), listOf(), {
        "loom.officialMojangMappings()"
    }),
    YARN(listOf(), listOf("https://maven.fabricmc.net/"), {
        val yarnVersions = httpClient.get("https://meta.fabricmc.net/v2/versions/yarn/$it").body<List<YarnVersion>>()
        "\"${yarnVersions.first().maven}:v2\""
    })
}