package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import ru.kontur.kinfra.gradle.presets.util.addDependency
import ru.kontur.kinfra.gradle.presets.util.addDependencyConstraint
import ru.kontur.kinfra.gradle.presets.util.configureEach
import ru.kontur.kinfra.gradle.presets.util.provideDelegate
import java.lang.Runtime.Version

object KotlinPreset : Preset {

    override fun Project.configure() {
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val kotlinVersion = getPluginVersion()

            configureCompiler(Version.parse(kotlinVersion))
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

        return checkNotNull(getKotlinPluginVersion())
    }

    private fun Project.configureCompiler(version: Version) {
        val additionalCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xjvm-default=all-compatibility",
            if (version < Version.parse("1.6.20")) {
                "-Xopt-in=kotlin.RequiresOptIn"
            } else {
                "-opt-in=kotlin.RequiresOptIn"
            }
        )
        tasks.configureEach<KotlinJvmCompile> { task ->
            with(task.kotlinOptions) {
                freeCompilerArgs += additionalCompilerArgs
                javaParameters = true
            }
        }

        val javaConvention = convention.getPlugin(JavaPluginConvention::class.java)
        afterEvaluate {
            tasks.configureEach<KotlinJvmCompile> { task ->
                task.kotlinOptions.jvmTarget = javaConvention.targetCompatibility.toString()
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
