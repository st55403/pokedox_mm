@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "eu.golovkov.pokedex"
    compileSdk = 34

    defaultConfig {
        applicationId = "eu.golovkov.pokedex"
        minSdk = 29
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.core.datastore)
    implementation(projects.core.database)

    implementation(projects.feature.pokemondetails)
    implementation(projects.feature.pokemonfilter)
    implementation(projects.feature.pokemonlist)

    implementation(libs.compose.destination.core)
    ksp(libs.compose.destination.ksp)
    implementation(libs.koin)
    implementation(libs.bundles.ktor)

    implementation(libs.core.ktx)
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.bundles.lifecycle)
}