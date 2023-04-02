package me.obsilabor.torch.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import me.obsilabor.torch.httpClient
import me.obsilabor.torch.model.MavenMetadata

object MavenCentralResolver : RepositoryResolver {
    override suspend fun resolveDependency(groupId: String, artifactId: String): String {
        return "$groupId:$artifactId:${resolveVersion(groupId, artifactId)}"
    }

    override suspend fun resolveVersion(groupId: String, artifactId: String): String {
        return httpClient.get("https://search.maven.org/solrsearch/select") {
            parameter("q", artifactId)
            parameter("rows", 20)
            parameter("wt", "json")
        }.body<MavenMetadata>().response.docs.first { it.id == "$groupId:$artifactId" }.latestVersion
    }
}