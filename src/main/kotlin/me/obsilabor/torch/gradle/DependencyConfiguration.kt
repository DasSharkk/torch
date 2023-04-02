package me.obsilabor.torch.gradle

enum class DependencyConfiguration(val function: String) {
    COMPILE_ONLY("compileOnly"),
    IMPLEMENTATION("implementation"),
    MINECRAFT("minecraft"),
    MAPPINGS("mappings"),
    MOD_IMPLEMENTATION("modImplementation"),
    MOD_RUNTIME_ONLY("modRuntimeOnly"),
    MOD_API("modApi"),
    INCLUDE("include"),
    PAPER_DEV_BUNDLE("paperweight.paperDevBundle"),
    DEV_BUNDLE("paperweight.devBundle"),
}