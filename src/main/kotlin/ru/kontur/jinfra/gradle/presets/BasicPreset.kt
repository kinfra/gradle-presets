package ru.kontur.jinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import ru.kontur.jinfra.gradle.presets.util.extension
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
