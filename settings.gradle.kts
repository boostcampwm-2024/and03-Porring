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
include(":core:common")
include(":core:navigation")
include(":core:designsystem")
include(":core:datastore")
include(":core:network")

include(":feature:home")
include(":feature:follower")
include(":feature:my")
include(":feature:search")
include(":feature:camera")
include(":feature:login")
include(":feature:detail")
include(":feature:upload")
include(":feature:setting")
include(":feature:their")
include(":feature:join")
include(":feature:main")
include(":app-test-camera")
