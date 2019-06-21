package ru.kontur.jinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.util.VersionNumber
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import ru.kontur.jinfra.gradle.presets.util.*

object KotlinPreset : Preset {

    private const val kotlinJvmTarget = "1.8"

    override fun Project.configure() {
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            configureCompiler()
            addStdlibDependency()
        }
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

    private fun Project.addStdlibDependency() {
        val kotlinVersion = VersionNumber.parse(getKotlinPluginVersion()!!).toString()

        val implementation by configurations

        // Add dependency on Kotlin stdlib
        addDependency(implementation, "org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)

        // Add constraint for kotlin-reflect to use correct version of it
        addDependencyConstraint(implementation, "org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    }

}
