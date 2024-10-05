plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.absensi_pln_dev"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.absensi_pln_dev"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Set Compose Compiler version
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.monitor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler) // Room uses annotation processing, so kapt is needed here

    // Compose dependencies
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.tooling.preview)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.compose.tooling)


    implementation("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Room KTX for Coroutines support
    implementation("androidx.room:room-ktx:2.6.0") // or the latest version

    // Google Maps
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // or your preferred version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // or your preferred version


    // ViewModel support for Compose
    implementation(libs.androidx.compose.viewmodel)
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14") // Use appropriate version
    val mapsComposeVersion = "4.3.2"
    implementation("com.google.maps.android:maps-compose:$mapsComposeVersion")
    implementation("com.google.maps.android:maps-compose-utils:$mapsComposeVersion")
    implementation("com.google.maps.android:maps-compose-widgets:$mapsComposeVersion")

}