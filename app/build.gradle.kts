import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.anaraya.anaraya"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.anaraya.anaraya"
        minSdk = 24
        targetSdk = 34
        versionCode = 66
        versionName = "1.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL")}\"")
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
        dataBinding = true
        buildConfig = true
    }
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //compose
    implementation("androidx.activity:activity-compose:1.9.0")

    //dotsIndicator
    implementation("com.tbuonomo:dotsindicator:5.0")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.5.0")

    //encrypted sharedPref
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    //piccaso
    implementation ("com.squareup.picasso:picasso:2.8")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    //navigation view
    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

    //paging3
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    // alternatively - without Android dependencies for tests
    testImplementation("androidx.paging:paging-common-ktx:3.2.1")

    //reveal layout
    implementation("com.chauthai.swipereveallayout:swipe-reveal-layout:1.4.1")

    //otp layout
    implementation ("com.github.mukeshsolanki.android-otpview-pinview:otpview:3.1.0")

    implementation(project(":domain"))
    implementation(project(":data"))
}