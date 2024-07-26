pluginManagement {
    includeBuild("build-logic")
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "runique"
include(":app")
include(":auth:data")
include(":auth:presentation")
include(":auth:domain")
include(":core:presentation:design-system")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":run:data")
include(":run:domain")

include(":run:presentation")
include(":run:location")
include(":run:network")

include(":core:presentation:ui")
include(":analytics:data")
include(":analytics:domain")
include(":analytics:presentation")
include(":analytics:analytics_feature")
include(":wear:app")
