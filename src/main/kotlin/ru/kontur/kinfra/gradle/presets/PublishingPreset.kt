package ru.kontur.kinfra.gradle.presets

import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.plugins.PublishingPlugin
import ru.kontur.kinfra.gradle.presets.util.extension
import ru.kontur.kinfra.gradle.presets.util.get

object PublishingPreset : Preset {

    const val INSTALL_TASK_NAME = "install"

    private const val PUBLICATION_NAME = "maven"
    private const val POM_PROJECT_EXTENSION = "pom"

    override fun Project.configure() {
        pluginManager.withPlugin("maven-publish") {
            configurePublishing()
            createInstallTask()
        }
    }

    private fun Project.configurePublishing() {
        val publication = getPrimaryPublication(this)

        configurePublication(publication)

        createPomShortcut(publication.pom)
    }

    private fun Project.configurePublication(publication: MavenPublication) {
        pluginManager.withPlugin("base") {
            setupArtifactId(publication)
        }

        pluginManager.withPlugin("java-library") {
            // Include Java Library in the publication, if present
            publication.from(components["java"])
        }

        pluginManager.withPlugin("java-platform") {
            // Include Java Platform in the publication, if present
            publication.from(components["javaPlatform"])
        }
    }

    /**
     * Use `archivesBaseName` as default `artifactId`
     */
    private fun Project.setupArtifactId(publication: MavenPublication) {
        val basePluginConvention = convention.getPlugin(BasePluginConvention::class.java)

        @Suppress("UnstableApiUsage")
        val baseNameProvider = provider {
            basePluginConvention.archivesBaseName
        }

        // We need to access artifactId as a Property
        publication as MavenPublicationInternal

        publication.mavenProjectIdentity.artifactId.set(baseNameProvider)
    }

    private fun Project.createPomShortcut(pom: MavenPom) {
        extensions.add(MavenPom::class.java, POM_PROJECT_EXTENSION, pom)
    }

    private fun Project.createInstallTask() {
        tasks.register(INSTALL_TASK_NAME) { task ->
            val delegateTask = tasks[MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME]

            task.group = PublishingPlugin.PUBLISH_TASK_GROUP
            task.description = "Alias for ${delegateTask.name}: " + delegateTask.description

            task.dependsOn(delegateTask)
        }
    }

    /**
     * Returns publication configured by this preset.
     *
     * Note that `maven-publish` plugin must be applied to the project.
     */
    fun getPrimaryPublication(project: Project): MavenPublication {
        return project.extension<PublishingExtension>().run {
            publications.maybeCreate(PUBLICATION_NAME, MavenPublication::class.java)
        }
    }

}
