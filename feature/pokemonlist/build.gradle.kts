@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "eu.golovkov.feature.pokemonlist"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

ksp {
    arg("compose-destinations.mode", "navgraphs")
    arg("compose-destinations.moduleName", "pokemonlist")
    arg("compose-destinations.useComposableVisibility", "true")
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.feature.pokemondetails)
    implementation(projects.feature.pokemonfilter)
    implementation(libs.compose.destination.core)
    implementation(libs.paging.compose)
    ksp(libs.compose.destination.ksp)
    implementation(libs.core.ktx)
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.bundles.lifecycle)
    implementation(libs.koin)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.material)
    implementation(libs.bundles.ui)
    implementation(libs.coil)
    implementation(libs.coil.svg)
}