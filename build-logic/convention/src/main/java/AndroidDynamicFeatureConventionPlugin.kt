import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.codeancy.convention.ExtensionType
import com.codeancy.convention.addUiLayerDependencies
import com.codeancy.convention.configureAndroidCompose
import com.codeancy.convention.configureBuildTypes
import com.codeancy.convention.configureKotlinAndroid
import com.codeancy.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidDynamicFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        target.run {

            pluginManager.run {
                apply("com.android.dynamic-feature")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<DynamicFeatureExtension> {

                configureKotlinAndroid(this)

                configureBuildTypes(this, ExtensionType.DYNAMIC_FEATURE)

                configureAndroidCompose(this)
            }

            dependencies {
                addUiLayerDependencies(target)
                "testImplementation"(kotlin("test"))
            }

        }
    }

}