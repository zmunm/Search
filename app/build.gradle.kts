import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    android
    `kotlin-android`
    `kotlin-kapt`
    id("dagger.hilt.android.plugin")
    detekt
}

val restKey: String = gradleLocalProperties(rootDir).getProperty(REST_KEY)

android {
    compileSdkVersion(Version.ANDROID_COMPILE)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "io.github.zmunm.search"
        minSdkVersion(Version.ANDROID_MIN)
        targetSdkVersion(Version.ANDROID_TARGET)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", REST_KEY, "\"$restKey\"")
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    packagingOptions {
        exclude("DebugProbesKt.bin")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Dependency.ANDROID_STARTUP)
    implementation(Dependency.ANDROID_KTX)
    implementation(Dependency.ANDROID_FRAGMENT_KTX)
    implementation(Dependency.ANDROID_APPCOMPAT)
    implementation(Dependency.ANDROID_MATERIAL)
    implementation(Dependency.ANDROID_CONSTRAINT)
    implementation(Dependency.ANDROID_SWIPE_REFRESH)

    implementation(Dependency.LIFECYCLE)
    implementation(Dependency.LIFECYCLE_LIVEDATA)
    implementation(Dependency.LIFECYCLE_VIEWMODEL)
    implementation(Dependency.LIFECYCLE_SAVED_STATE)

    implementation(Dependency.HILT)
    kapt(Dependency.HILT_APT)

    implementation(Dependency.COROUTINE)

    implementation(Dependency.PAGING)

    implementation(Dependency.GLIDE)
    kapt(Dependency.GLIDE_APT)

    implementation(Dependency.TIMBER)

    testImplementation(Dependency.COROUTINE_TEST)
    testImplementation(Dependency.KOTEST)
    testImplementation(Dependency.MOCKK)
}
