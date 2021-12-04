import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"
    defaultConfig {
        applicationId = "com.snowdango.musiclogger"
        minSdk = 28
        targetSdk = 30
        versionCode = 12
        versionName = "2.1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug"){
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_1_8
        targetCompatibility =  JavaVersion.VERSION_1_8
    }
    buildFeatures{
        dataBinding = true
        viewBinding = true
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta03"
    }
    kapt{
        correctErrorTypes = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.preference:preference-ktx:1.1.1")

    // Kotlin
    val navVersion = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // compose
    val composeVersion = "1.1.0-beta03"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha02")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.compiler:compiler:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.navigation:navigation-compose:2.4.0-beta02")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02")
    implementation("androidx.activity:activity-compose:1.4.0")

    // api req
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.0.0")

    // db room
    implementation("androidx.room:room-runtime:2.4.0-rc01")
    kapt("androidx.room:room-compiler:2.4.0-rc01")

    // time klock
    implementation("com.soywiz.korlibs.klock:klock-android:2.2.0")

    // Koin main features for Android
    val koinVersion = "3.1.3"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation("io.insert-koin:koin-android-compat:$koinVersion")
    implementation("io.insert-koin:koin-androidx-workmanager:$koinVersion")
    implementation("io.insert-koin:koin-androidx-navigation:$koinVersion")

    // epoxy
    val epoxyVersion = "4.6.3"
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    implementation("com.airbnb.android:epoxy-databinding:$epoxyVersion")
    kapt("com.airbnb.android:epoxy-processor:$epoxyVersion")

    // landscapist
    implementation("com.github.skydoves:landscapist-glide:1.4.3")

    //card
    implementation("androidx.cardview:cardview:1.0.0")

    testImplementation("junit:junit:")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
