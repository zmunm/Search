plugins {
    kotlin
    detekt
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(group = "javax.inject", name = "javax.inject", version = "1")
    implementation(Dependency.COROUTINE)

    implementation(Dependency.TIMBER)

    testImplementation(Dependency.KOTEST)
    testImplementation(Dependency.MOCKK)
    testImplementation(Dependency.COROUTINE_TEST)
}
