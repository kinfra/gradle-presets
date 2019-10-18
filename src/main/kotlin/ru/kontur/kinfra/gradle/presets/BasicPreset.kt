package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import java.util.concurrent.TimeUnit

object BasicPreset : Preset {

    override fun Project.configure() {
        configureVersionsResolution()
        configureDefaultRepositories()
    }

    private fun Project.configureVersionsResolution() {
        configurations.all { configuration ->
            with(configuration.resolutionStrategy) {
                // Always update snapshots and dynamic versions
                cacheChangingModulesFor(0, TimeUnit.SECONDS)
                cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
            }
        }
    }

    private fun Project.configureDefaultRepositories() {
        with(repositories) {
            if (isEmpty()) {
                mavenLocal()
                jcenter()
            }
        }
    }

}
