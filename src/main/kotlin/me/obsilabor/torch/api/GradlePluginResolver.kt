package me.obsilabor.torch.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.obsilabor.torch.httpClient

class GradlePluginResolver(private val baseUrl: String = "https://plugins.gradle.org/m2/", private val reverse: Boolean = false) : RepositoryResolver {

    override suspend fun resolveDependency(groupId: String, artifactId: String): String {
        return "$groupId.$artifactId:${resolveVersion(groupId, artifactId)}"
    }

    override suspend fun resolveVersion(groupId: String, artifactId: String): String {
        runCatching {
            var url = baseUrl +
                    groupId.replace(".", "/") +
                    "/$groupId.$artifactId/maven-metadata.xml"
            var response = httpClient.get(url)
            if (response.status == HttpStatusCode.NotFound) {
                url = baseUrl +
                        groupId.replace(".", "/") +
                        "/$artifactId/maven-metadata.xml"
                response = httpClient.get(url)
            }
            return if (!reverse) {
                response.bodyAsText().lines()
                    .first { it.contains("<version>") }
                    .split("<version>")[1]
                    .removeSuffix("</version>")
            } else {
                response.bodyAsText().lines()
                    .last { it.contains("<version>") }
                    .split("<version>")[1]
                    .removeSuffix("</version>")
            }

        }
        return "error"
    }
}