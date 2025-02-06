import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.todoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todoapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.microsoft.identity.client:msal:4.10.0") {
        exclude(group = "io.opentelemetry")
        exclude(group = "com.microsoft.device.display")
    }
    implementation("io.opentelemetry:opentelemetry-api:1.18.0")
    implementation("io.opentelemetry:opentelemetry-context:1.18.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
    implementation(libs.compiler)
    implementation(libs.contentpager)
    implementation(libs.mediation.test.suite)
}
