plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

object PluginVersion {
    const val ANDROID_TOOL = "4.2.1"
    const val KOTLIN = "1.5.10"
    const val DOKKA = "1.4.32"
    const val DETEKT = "1.17.1"
    const val HILT = "2.35.1"
    const val VERSIONING = "0.38.0"
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginVersion.ANDROID_TOOL}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersion.KOTLIN}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${PluginVersion.KOTLIN}")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${PluginVersion.DETEKT}")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:${PluginVersion.DOKKA}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${PluginVersion.VERSIONING}")
    implementation("com.google.dagger:hilt-android-gradle-plugin:${PluginVersion.HILT}")
}