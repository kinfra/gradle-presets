package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.jvm.toolchain.JavaLanguageVersion
import ru.kontur.kinfra.gradle.presets.util.*

object JvmPreset : Preset {

    private const val junitVersion = "5.9.3"
    private const val javaVersion = 17

    override fun Project.configure() {
        pluginManager.withPlugin("java") {
            configureJava()
            configureJunit()
        }
    }

    private fun Project.configureJava() {
        extension<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
        }
    }

    private fun Project.configureJunit() {
        tasks.configureEach<Test> {
            // Show stacktrace in console when a test fails
            testLogging {
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
            }

            // Use JUnit Platform (JUnit 5) for tests
            useJUnitPlatform()
        }

        addJunitDependencies()
    }

    private fun Project.addJunitDependencies() {
        val testImplementation by configurations
        val testRuntimeOnly by configurations
        addPlatformDependency(testImplementation, "org.junit", "junit-bom", junitVersion)
        addDependency(testImplementation, "org.junit.jupiter", "junit-jupiter-api")
        addDependency(testImplementation, "org.junit.jupiter", "junit-jupiter-params")
        addDependency(testRuntimeOnly, "org.junit.jupiter", "junit-jupiter-engine")
    }

}
