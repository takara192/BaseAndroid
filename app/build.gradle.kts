plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("androidx.room")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.colorphone"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.colorphone"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Android Core
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
    implementation("androidx.work:work-runtime-ktx:2.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    //Dagger hilt
    val hilt_version = "2.60.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    ksp("com.google.dagger:hilt-android-compiler:$hilt_version")

    //Retrofit + okhttp
    val retrofit_version = "3.0.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:5.5.0")

    //Navigation component
    val nav_version = "2.10.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //ViewModel + LiveData + Corountine
    val lifecycle_version = "2.11.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    ksp("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    val coroutinesVersion = "1.11.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    //RoomDatabase
    val room = "2.8.5"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    ksp("androidx.room:room-compiler:$room")

    // View
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.airbnb.android:lottie:6.7.1")

    // Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Loading Image
    implementation("io.coil-kt.coil3:coil:3.6.3")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.6.3")

    // Startup
    implementation("androidx.startup:startup-runtime:1.2.0")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
}