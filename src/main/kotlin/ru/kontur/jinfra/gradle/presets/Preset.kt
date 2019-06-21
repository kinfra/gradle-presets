package ru.kontur.jinfra.gradle.presets

import org.gradle.api.Project

interface Preset {

    fun Project.configure()

}

fun Project.apply(preset: Preset) {
    with(preset) {
        configure()
    }
}
