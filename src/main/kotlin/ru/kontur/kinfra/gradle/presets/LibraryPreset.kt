package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import ru.kontur.kinfra.gradle.presets.util.extension

object LibraryPreset : Preset {

    override fun Project.configure() {
        pluginManager.withPlugin("java-library") {
            configureSourcesJar()
        }
    }

    private fun Project.configureSourcesJar() {
        extension<JavaPluginExtension> {
            withSourcesJar()
        }
    }

}
