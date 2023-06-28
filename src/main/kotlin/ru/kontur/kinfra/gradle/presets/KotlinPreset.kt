package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import ru.kontur.kinfra.gradle.presets.util.addDependency
import ru.kontur.kinfra.gradle.presets.util.addDependencyConstraint
import ru.kontur.kinfra.gradle.presets.util.configureEach
import ru.kontur.kinfra.gradle.presets.util.provideDelegate

object KotlinPreset : Preset {

    override fun Project.configure() {
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val kotlinVersion = getPluginVersion()

            configureCompiler()
            addStdlibDependency(kotlinVersion)
        }
    }

    private fun Project.getPluginVersion(): String {
        try {
            Class.forName("org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt")
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(
                "Please add org.jetbrains.kotlin.jvm plugin to classpath of the build script " +
                        "that uses plugin ru.kontur.kinfra.presets"
            )
        }

        return getKotlinPluginVersion()
    }

    private fun Project.configureCompiler() {
        val additionalCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn",
        )
        tasks.configureEach<KotlinJvmCompile> {
            kotlinOptions {
                freeCompilerArgs += additionalCompilerArgs
                javaParameters = true
            }
        }
    }

    private fun Project.addStdlibDependency(kotlinVersion: String) {
        val implementation by configurations

        // Add dependency on Kotlin stdlib
        addDependency(implementation, "org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)

        // Add constraint for kotlin-reflect to use correct version of it
        addDependencyConstraint(implementation, "org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    }

}
