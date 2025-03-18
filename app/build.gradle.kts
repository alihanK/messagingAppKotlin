plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "com.package.msgapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.package.msgapp"
        minSdk = 25
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            pickFirsts.add("META-INF/DEPENDENCIES")
            pickFirsts.add("mozilla/public-suffix-list.txt")
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
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //coil
    implementation(libs.coil)



    // Firebase
    implementation(libs.firebase.crashlytics.buildtools)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation (libs.firebase.messaging)
    implementation (libs.google.auth.library.oauth2.http)

    // Compose Navigation
    implementation ("androidx.navigation:navigation-compose:2.8.9")

    // Material 3 (MyAppTheme için)
    implementation ("androidx.compose.material3:material3:1.3.1")

    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    // Activity Compose
    implementation(libs.androidx.activity.compose)



    implementation ("com.google.firebase:firebase-auth:23.2.0")

    implementation ("com.google.firebase:firebase-firestore-ktx")

    implementation ("androidx.compose.runtime:runtime-livedata:1.7.8")

    //dagger2
    implementation (libs.dagger)
    kapt (libs.daggerCompiler)

    implementation (libs.google.firebase.analytics)
    implementation (libs.google.firebase.database)

}