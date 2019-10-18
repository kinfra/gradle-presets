package ru.kontur.kinfra.gradle.presets

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import ru.kontur.kinfra.gradle.presets.util.*

object JvmPreset : Preset {

    private const val junitVersion = "5.4.2"
    private val javaVersion = JavaVersion.VERSION_1_8

    override fun Project.configure() {
        pluginManager.withPlugin("java") {
            configureJava()
            configureJunit()
        }
    }

    private fun Project.configureJava() {
        convention.getPlugin(JavaPluginConvention::class.java).run {
            sourceCompatibility = javaVersion
        }
    }

    private fun Project.configureJunit() {
        tasks.allWithType<Test> { task ->
            // Show stacktrace in console when a test fails
            with(task.testLogging) {
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
            }

            // Use JUnit Platform (JUnit 5) for tests
            @Suppress("UnstableApiUsage")
            task.useJUnitPlatform()
        }

        addJunitDependencies()
    }

    private fun Project.addJunitDependencies() {
        val testImplementation by configurations
        val testRuntimeOnly by configurations

        listOf(testImplementation, testRuntimeOnly).forEach { configuration ->
            addPlatformDependency(configuration, "org.junit", "junit-bom", junitVersion)
        }

        addDependency(testImplementation, "org.junit.jupiter", "junit-jupiter-api")
        addDependency(testImplementation, "org.junit.jupiter", "junit-jupiter-params")

        addDependency(testRuntimeOnly, "org.junit.jupiter", "junit-jupiter-engine")
    }

}
