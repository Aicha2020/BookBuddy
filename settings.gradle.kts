pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    // Remove plugins block entirely (better to declare versions in build.gradle)
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BookBuddy"  // Required for Gradle 7+
include(":app")  // Required to include your app module