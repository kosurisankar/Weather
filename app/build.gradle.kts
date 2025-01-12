
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")

}

android {
    namespace = "com.skworks.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skworks.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.play.services.location)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui.test.junit4.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.ui)
    implementation (libs.androidx.navigation.compose)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.datastore.preferences)
    // Retrofit and Coroutines
    implementation (libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.core)

    // Dagger-Hilt for Dependency Injection
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)
    // Image Caching
    implementation (libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockito.core.v3112)
    // Jetpack Compose UI Test
    androidTestImplementation (libs.ui.test.junit4)

    // For using Compose UI tests with navigation
    androidTestImplementation (libs.androidx.navigation.testing)

    // For using the testing rule
    androidTestImplementation (libs.ui.test)

    // Jetpack Compose Test
    testImplementation (libs.ui.test)

    // Mockito for Mocking
    testImplementation (libs.mockito.mockito.core)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.androidx.core.testing)
}