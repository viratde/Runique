plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.codeancy.analytics.presentation"
}

dependencies {

    implementation(projects.analytics.domain)
}