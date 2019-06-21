package ru.kontur.jinfra.gradle.presets.util

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

internal fun Project.addDependency(
    configuration: Configuration,
    group: String,
    name: String,
    version: String? = null
) {

    val coordinates = if (version != null) {
        "$group:$name:$version"
    } else {
        "$group:$name"
    }

    configuration.dependencies += dependencies.create(coordinates)
}

@Suppress("UnstableApiUsage")
internal fun Project.addPlatformDependency(
    configuration: Configuration,
    group: String,
    name: String,
    version: String
) {

    configuration.dependencies += dependencies.create(dependencies.platform("$group:$name:$version"))
}

@Suppress("UnstableApiUsage")
internal fun Project.addDependencyConstraint(
    configuration: Configuration,
    group: String,
    name: String,
    version: String
) {

    val constraint = dependencies.constraints.create("$group:$name:$version")
    configuration.dependencyConstraints += constraint
}
