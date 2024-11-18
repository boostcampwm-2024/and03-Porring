pluginManagement {

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Porring"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:model")
include(":core:data")
include(":core:domain")
include(":feature:main")
include(":feature:home")
include(":feature:follower")
include(":feature:my")
include(":feature:search")
include(":feature:camera")
include(":feature:login")
include(":core:navigation")
include(":app-test-camera")
include(":core:common")
include(":feature:detail")
include(":feature:detail")
