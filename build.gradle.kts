plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("application")
}

group = "me.obsilabor"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.5.2")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta7")

    implementation("io.ktor:ktor-client-core:2.2.3")
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.3")
    implementation("me.obsilabor:piston-meta-kt:1.0.6")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("me.obsilabor.torch.TorchAppKt")
}