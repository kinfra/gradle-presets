package ru.kontur.jinfra.gradle.presets

import org.gradle.api.Project

interface Preset {

    fun Project.configure()

}

internal fun Project.apply(preset: Preset) {
    with(preset) {
        configure()
    }
}
