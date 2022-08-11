# Presets for Gradle

This Gradle plugin applies common configuration that we use in our JVM projects at Kontur.
It aids to avoid boilerplate in build scripts.

The plugin consists of several configuration blocks (called presets)
which are mainly triggered when some relevant plugin is applied (e.g. `java` or `maven-publish`).

## Usage

```groovy
plugins {
    id 'ru.kontur.kinfra.presets' version '<version>'
}
```

Currently, the plugin requires Gradle version 6.7.

## Available presets

### Basic configuration (applied to any project)

#### Disabled caching of changing and dynamic modules

By default, Gradle caches dependencies that are changing (snapshots) 
or have dynamic versions (e.g. `1.+`) for 24 hours.
Such caching is disabled.

#### Default repositories

When there are no repositories configured in the project at the time the plugin applied,
following default repositories are added:

  * Local Maven cache (`mavenLocal`)

  * Sonatype's Maven Central (`mavenCentral`)

This behavior can be customized in two ways:

  * __Add additional repositories__
   
    To use other repositories in your project just declare them in `repositories { }` block.
   
  * __Use other repositories instead of default ones__
 
    To prevent use of these default repositories you need to add other repositories before
    application of the plugin.
    
    This can be done in [init script]:
   
    ```groovy
    allprojects {
        repostitories {
            maven { url = "repo.mycompany.com" }
        }
    }
    ```
   
    [init script]: https://docs.gradle.org/current/userguide/init_scripts.html
    
    Or in `settings.gradle`:
    
    ```groovy
    gradle.projectsLoaded {
        gradle.allprojects {
            repositories {
                maven { url = "repo.mycompany.com" }
            }
        }
    }
    ```

### Java configuration (applied with `java` plugin)

#### Java version is 11

For now, default Java version in our projects is 11.

#### Full exception info in tests

When a test fails, its exception's stack trace is being logged to console.
It helps to investigate failure on CI faster.

#### JUnit Platform for tests

We use JUnit 5 for in-project (unit and some integration) tests.

Dependency on a recent JUnit release is automatically added by the plugin.
To override it, add dependency on JUnit BOM of desired version manually:
```groovy
dependncies {
    testImplementation platform("org.junit:junit-bom:<version>")
}
```

### Kotlin configuration (applied with `org.jetbrains.kotlin.jvm` plugin)

#### Aligned JVM target

Kotlin's target JVM version is set to the same as Java's `targetCompatibility`.

#### Compiler arguments

The following options are added to Kotlin compiler command line by the plugin:

  * `-java-parameters`: generate metadata for Java reflection on method parameters.

  * `-Xjsr305=strict`: use JSR-305 nullability annotations. ([details][jsr-305])

  * `-Xjvm-default=all-compatibility`: compile non-abstract interface methods as Java [default methods][default-interop]. Will be changed to `all` later.

  * `-opt-in=kotlin.RequiresOptIn`: allow usage of `@OptIn` and `@RequiresOptIn` [annotations][opt-in].

  [jsr-305]: http://kotlinlang.org/docs/reference/java-interop.html#jsr-305-support
  [default-interop]: https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#default-methods-in-interfaces
  [opt-in]: https://kotlinlang.org/docs/opt-in-requirements.html

#### Stdlib dependency

Dependency on Kotlin stdlib is automatically added to `implementation` configuration.
Its version is the same as Kotlin plugin's one.

### Publishing configuration (applied with `maven-publish` plugin)

The plugin creates a `MavenPublication` named `maven` and adds a project component to it:

  * In a `java-library` project the `java` component is being added.

  * In a `java-platform` project the `javaPlatform` component is being added.
  
Usually our projects that are being published are either of these.

Publication's `artifactId` is changed to project's `archivesBaseName` instead of project's name.

Configuration of the POM can be accessed via `pom { }` block in the project, just as in `MavenPublication`.

Also, plugin creates a task named `install` as an alias for `publishToMavenLocal`.

### Library configuration (applied with `java-library` plugin)

#### Sources JAR

A task named `sourcesJar` is created in the project using Gradle's built-in [feature][sources-jar].

Obliviously it packages project's main sources into a JAR with `sources` classifier.

  [sources-jar]: https://docs.gradle.org/current/dsl/org.gradle.api.plugins.JavaPluginExtension.html#org.gradle.api.plugins.JavaPluginExtension:withSourcesJar()
