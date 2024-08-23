plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kspAndroid)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.example.domainlogicandroidtest.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 27

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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //unit testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)

    //android testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //dagger-hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

    //retrofit
    implementation(libs.bundles.android.retrofit.bundle)

    //navigation
    implementation(libs.bundles.android.navigation.bundle)

    //modules
    implementation(project( ":app:domain"))
}