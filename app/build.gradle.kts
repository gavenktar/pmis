@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ.19369 is fixed
plugins {
    kotlin("jvm") version "1.9.10" apply false
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization") version "1.9.10"

}


ksp {
    arg("room.schemaLocation", "$projectDir/schema")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

android {
    namespace = "by.bsuir.kirylarol.wolfquotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "by.bsuir.kirylarol.wolfquotes"
        minSdk = 26
        targetSdk = 33
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
                "proguard.rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META.INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all {
        addJavaSourceFoldersToModel(
            File(buildDir, "generated/ksp/$name/kotlin")
        )
    }
}

dependencies {

    implementation(libs.androidx.androidx.room.gradle.plugin)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)


    implementation(libs.androidx.ui.text)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.room.runtime)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
   implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
 //   implementation(libs.ktor.ktor.serialization)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
//    implementation(libs.kotlinx.serialization.json)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation(libs.androidx.monitor)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)
    ksp(libs.compose.destinations.ksp)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.ktor)
    //implementation(libs.bundles.serialization)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.lifecycle)
    implementation(libs.okhttp.logging)
    implementation(libs.paging.compose)
    implementation(libs.paging.common)
    implementation(libs.work)
    implementation(libs.datastore)
    implementation(libs.coil.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.collection)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.destinations)
    debugImplementation(libs.compose.tooling)

    runtimeOnly(libs.ktor.client.cio)



}