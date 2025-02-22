import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.guestify"
    compileSdk = 35
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.guestify"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())

            buildConfigField(
                "String",
                "MAPS_API_KEY",
                "\"${localProperties.getProperty("MAPS_API_KEY", "")}\""
            )
            manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY", "")
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                debug {
                    isDebuggable = true
                }
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    dependencies {

// Confetti library
        implementation(libs.konfetti)
// Lottie for advanced loading animations
        implementation(libs.lottie)
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)
        implementation(libs.roomRuntime)
        implementation("androidx.room:room-ktx:2.5.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
        implementation(libs.firebase.firestore.ktx)
        implementation(libs.places)
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
        kapt(libs.roomCompiler)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation("com.github.bumptech.glide:glide:4.16.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
        implementation("com.google.dagger:hilt-android:2.48")
        kapt("com.google.dagger:hilt-compiler:2.48")
        implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore")
        implementation("com.google.android.gms:play-services-auth:20.7.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.google.android.libraries.places:places:3.4.0")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.google.android.libraries.places:places:3.4.0")
    }
}