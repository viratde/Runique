package com.codeancy.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {

    commonExtension.run {

        buildFeatures {
            buildConfig = true
        }

        val apiKey = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
        val mapsApiKey = gradleLocalProperties(rootDir, providers).getProperty("MAPS_API_KEY")
        when (extensionType) {

            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey)
                            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                        }
                        release {
                            configureReleaseBuildType(apiKey, commonExtension)
                            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey)
                            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                        }
                        release {
                            configureReleaseBuildType(apiKey, commonExtension)
                            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey)
                            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                        }
                        release {
                            configureReleaseBuildType(apiKey, commonExtension,false)
                            manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
                        }
                    }
                }
            }
        }

    }
}

private fun BuildType.configureDebugBuildType(
    apiKey: String
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://runique.pl-coding.com:8080\"")
}

private fun BuildType.configureReleaseBuildType(
    apiKey: String,
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    minifyEnabled:Boolean = true
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://runique.pl-coding.com:8080\"")
    isMinifyEnabled = minifyEnabled
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )

}