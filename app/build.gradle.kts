plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kspCompose)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.andruszkiewicz.internetshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.andruszkiewicz.internetshop"
        minSdk = 24
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    //okHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    //Dagger Hilt
    implementation(libs.hilt)
//    implementation(libs.hilt.lifecycle.viewmodel)
    ksp(libs.hilt.compiler)

    //Lifecycle
    implementation(libs.lifecycle.viewModel)
}