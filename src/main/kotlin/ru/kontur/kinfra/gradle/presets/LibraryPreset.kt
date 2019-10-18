package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import ru.kontur.kinfra.gradle.presets.util.get

object LibraryPreset : Preset {

    override fun Project.configure() {
        pluginManager.withPlugin("java-library") {
            configureSourcesJar()
        }
    }

    private fun Project.configureSourcesJar() {
        val allSource = convention.getPlugin(JavaPluginConvention::class.java).run {
            sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].allSource
        }

        val task = tasks.create("sourcesJar", Jar::class.java).apply {
            group = BasePlugin.BUILD_GROUP
            description = "Assemble a JAR containing all sources of the module."
            archiveClassifier.set("sources")

            from(allSource)

            dependsOn(tasks[JavaPlugin.CLASSES_TASK_NAME])
        }

        registerArtifact(task)
    }

    private fun Project.registerArtifact(task: Task) {
        tasks[BasePlugin.ASSEMBLE_TASK_NAME].dependsOn(task)

        pluginManager.withPlugin("maven-publish") {
            PublishingPreset.getPrimaryPublication(this).apply {
                artifact(task)
            }
        }
    }

}
