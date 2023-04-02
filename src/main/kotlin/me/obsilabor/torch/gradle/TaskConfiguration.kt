package me.obsilabor.torch.gradle

data class TaskConfiguration(
    val type: ConfigurationType,
    val key: String,
    val value: String
)

enum class ConfigurationType {
    ASSIGN,
    INVOKE
}