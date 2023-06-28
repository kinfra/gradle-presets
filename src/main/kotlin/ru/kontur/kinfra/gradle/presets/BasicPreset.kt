package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories
import java.util.concurrent.TimeUnit

object BasicPreset : Preset {

    override fun Project.configure() {
        configureVersionsResolution()
        configureDefaultRepositories()
    }

    private fun Project.configureVersionsResolution() {
        configurations.all {
            resolutionStrategy {
                // Always update snapshots and dynamic versions
                cacheChangingModulesFor(0, TimeUnit.SECONDS)
                cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
            }
        }
    }

    private fun Project.configureDefaultRepositories() {
        repositories {
            if (isEmpty()) {
                mavenLocal()
                mavenCentral()
            }
        }
    }

}
