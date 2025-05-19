plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.akua.demo.dynamicloadso"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.akua.demo.dynamicloadso"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
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
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

tasks.register("BuildNativeLibToRootOnly") {
    group = "build"
    description = "Builds the native library and places it in the root directory."
    dependsOn("externalNativeBuildRelease")
    doLast {
        copy {
            from("$buildDir/intermediates/cmake/release/obj")
            into("$rootDir/outputs")
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}