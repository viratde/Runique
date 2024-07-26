plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.codeancy.auth.presentation"
}

dependencies {

    implementation(projects.auth.domain)
    implementation(projects.core.domain)

    implementation(libs.bundles.koin)

}