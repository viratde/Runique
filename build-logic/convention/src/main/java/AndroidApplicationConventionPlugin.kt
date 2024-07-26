import com.android.build.api.dsl.ApplicationExtension
import com.codeancy.convention.ExtensionType
import com.codeancy.convention.configureBuildTypes
import com.codeancy.convention.configureKotlinAndroid
import com.codeancy.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        target.run {

            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {


                defaultConfig {

                    applicationId = libs.findVersion("applicationId").get().toString()

                    targetSdk = libs.findVersion("targetSdkVersion").get().toString().toInt()

                    versionCode = libs.findVersion("versionCode").get().toString().toInt()
                    versionName = libs.findVersion("versionName").get().toString()
                }

                configureKotlinAndroid(this)

                configureBuildTypes(this, ExtensionType.APPLICATION)


            }


        }
    }

}