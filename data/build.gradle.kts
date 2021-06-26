import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    `android-library`
    `kotlin-android`
    `kotlin-kapt`
    detekt
}

android {
    compileSdkVersion(Version.ANDROID_COMPILE)

    defaultConfig {
        minSdkVersion(Version.ANDROID_MIN)
        targetSdkVersion(Version.ANDROID_TARGET)
    }

    testOptions {
        unitTests {
            all {
                it.useJUnitPlatform()
                it.testLogging {
                    events("passed", "skipped", "failed")
                    it.outputs.upToDateWhen {
                        false
                    }
                    showStandardStreams = true
                    showCauses = true
                    showExceptions = true
                    showStackTraces = true
                    exceptionFormat = TestExceptionFormat.FULL
                }
            }
        }
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependency.COROUTINE)

    implementation(Dependency.HILT)
    kapt(Dependency.HILT_APT)

    implementation(Dependency.RETROFIT)
    implementation(Dependency.RETROFIT_MOSHI)
    implementation(Dependency.RETROFIT_INTERCEPTOR)

    implementation(Dependency.MOSHI)
    implementation(Dependency.MOSHI_ADAPTER)
    kapt(Dependency.MOSHI_APT)

    implementation(Dependency.ROOM_RUNTIME)
    implementation(Dependency.ROOM_KTX)
    kapt(Dependency.ROOM_COMPILER)

    implementation(Dependency.TIMBER)

    testImplementation(Dependency.KOTEST)
    testImplementation(Dependency.MOCKK)
    testImplementation(Dependency.COROUTINE_TEST)
    testImplementation(Dependency.MOCK_WEBSERVER)
}
