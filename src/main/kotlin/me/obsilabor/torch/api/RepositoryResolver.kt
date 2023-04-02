package me.obsilabor.torch.api

interface RepositoryResolver {

    suspend fun resolveDependency(groupId: String, artifactId: String): String

    suspend fun resolveVersion(groupId: String, artifactId: String): String
}