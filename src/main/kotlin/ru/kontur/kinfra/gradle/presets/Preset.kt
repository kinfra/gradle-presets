package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project

interface Preset {

    fun Project.configure()

}

internal fun Project.apply(preset: Preset) {
    with(preset) {
        configure()
    }
}
