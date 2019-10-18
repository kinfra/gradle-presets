package ru.kontur.kinfra.gradle.presets.util

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.tasks.TaskContainer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/*
 * Convenient extensions for Gradle API objects.
 */

internal inline fun <reified T : Any> Project.extension(block: T.() -> Unit = {}): T {
    return extensions.getByType(T::class.java).apply(block)
}

internal inline fun <reified T : Task> TaskContainer.allWithType(noinline block: (T) -> Unit) {
    withType(T::class.java).all(block)
}

internal operator fun ConfigurationContainer.provideDelegate(
    thisRef: Any?,
    property: KProperty<*>
): ReadOnlyProperty<Any?, Configuration> {

    val configuration = getByName(property.name)

    return object : ReadOnlyProperty<Any?, Configuration> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Configuration {
            return configuration
        }
    }
}

internal operator fun <T> NamedDomainObjectSet<T>.get(name: String): T = getByName(name)
