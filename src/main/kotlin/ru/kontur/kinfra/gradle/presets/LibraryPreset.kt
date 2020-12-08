package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.*
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import ru.kontur.kinfra.gradle.presets.util.extension
import ru.kontur.kinfra.gradle.presets.util.get

object LibraryPreset : Preset {

    override fun Project.configure() {
        pluginManager.withPlugin("java-library") {
            configureSourcesJar()
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.configureSourcesJar() {
        extension<JavaPluginExtension> {
            withSourcesJar()
        }
    }

}
