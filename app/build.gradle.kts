plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    id("kotlin-parcelize")
}

android {
    namespace = "com.winnix.base"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.winnix.base"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "ENABLE_LOG", "true")
            // Debug-only switch between the fake and the real remote data sources; see src/debug.
            buildConfigField("boolean", "USE_FAKE_DATA", "true")
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "ENABLE_LOG", "false")
        }
        // Release behaviour without R8, so it builds fast; logs stay on for testing.
        create("staging") {
            initWith(getByName("release"))
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = false
            matchingFallbacks += "release"
            buildConfigField("boolean", "ENABLE_LOG", "true")
        }
    }
    sourceSets {
        // Staging and release share the real data source bindings; only debug sees the fake ones.
        getByName("staging").kotlin.srcDir("src/prod/java")
        getByName("release").kotlin.srcDir("src/prod/java")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.work.runtime.ktx)

    // Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Retrofit + OkHttp + Serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // Navigation component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Lifecycle + Coroutines
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // View
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.lottie)

    // Logging
    implementation(libs.timber)

    // Loading Image
    implementation(libs.coil)
    implementation(libs.coil.network.okhttp)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
