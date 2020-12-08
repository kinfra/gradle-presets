package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.util.VersionNumber

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
        val currentGradleVersion = VersionNumber.parse(gradle.gradleVersion)
        if (currentGradleVersion < VersionNumber.parse(minGradleVersion)) {
            throw RuntimeException(
                "Plugin ru.kontur.kinfra.presets require Gradle $minGradleVersion+ (current is $currentGradleVersion)"
            )
        }
    }

    companion object {
        private const val minGradleVersion = "6.7"
    }

}
