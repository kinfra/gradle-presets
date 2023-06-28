package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

class KinfraPresetsPlugin : Plugin<Project> {

    private val presets = listOf(
        BasicPreset,
        JvmPreset,
        KotlinPreset,
        PublishingPreset,
        LibraryPreset
    )

    override fun apply(project: Project) {
        checkGradleVersion(project.gradle)

        for (preset in presets) {
            project.apply(preset)
        }
    }

    private fun checkGradleVersion(gradle: Gradle) {
        val currentVersion = gradle.gradleVersion
        val currentMajorVersion = currentVersion.takeWhile { it.isDigit() }.toInt()
        if (currentMajorVersion < minGradleVersion) {
            throw RuntimeException(
                "Plugin ru.kontur.kinfra.presets requires Gradle $minGradleVersion+ (current is $currentVersion)"
            )
        }
    }

    companion object {
        private const val minGradleVersion = 8
    }

}
