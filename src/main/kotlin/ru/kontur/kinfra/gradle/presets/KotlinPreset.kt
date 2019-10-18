package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import ru.kontur.kinfra.gradle.presets.util.*

object KotlinPreset : Preset {

    private const val kotlinJvmTarget = "1.8"

    override fun Project.configure() {
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val kotlinVersion = getPluginVersion()

            configureCompiler()
            addStdlibDependency(kotlinVersion)
        }
    }

    private fun Project.getPluginVersion(): String {
        try {
            Class.forName("org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper")
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(
                "Please add org.jetbrains.kotlin.jvm plugin to classpath of the build script " +
                        "that uses plugin ru.kontur.kinfra.presets"
            )
        }

        val kotlinPlugin = plugins.getPlugin(KotlinPluginWrapper::class.java)
        return kotlinPlugin.kotlinPluginVersion
    }

    private fun Project.configureCompiler() {
        tasks.allWithType<KotlinJvmCompile> { task ->
            with(task.kotlinOptions) {
                jvmTarget = kotlinJvmTarget
                freeCompilerArgs += listOf(
                    "-Xjsr305=strict",
                    "-Xjvm-default=enable"
                )
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
