plugins {
    `kotlin-dsl`
}

group = "com.codeancy.runique.buildLogic"


dependencies {

    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)

    gradlePlugin {
        plugins {
            register("androidApplication") {
                id = "runique.android.application"
                implementationClass = "AndroidApplicationConventionPlugin"
            }
            register("androidComposeApplication") {
                id = "runique.android.application.compose"
                implementationClass = "AndroidApplicationComposeConventionPlugin"
            }
            register("androidLibrary") {
                id = "runique.android.library"
                implementationClass = "AndroidLibraryConventionPlugin"
            }
            register("androidDynamicFeature") {
                id = "runique.android.dynamic-feature"
                implementationClass = "AndroidDynamicFeatureConventionPlugin"
            }
            register("androidComposeLibrary") {
                id = "runique.android.library.compose"
                implementationClass = "AndroidLibraryComposeConventionPlugin"
            }
            register("androidFeatureUi") {
                id = "runique.android.feature.ui"
                implementationClass = "AndroidFeatureUiConventionPlugin"
            }
            register("androidRoom"){
                id = "runique.android.room"
                implementationClass = "AndroidRoomConventionPlugin"
            }
            register("JvmLibrary"){
                id = "runique.jvm.library"
                implementationClass = "JvmLibraryConventionPlugin"
            }
            register("JvmKtor"){
                id = "runique.jvm.ktor"
                implementationClass = "JvmKtorConventionPlugin"
            }
        }
    }
}