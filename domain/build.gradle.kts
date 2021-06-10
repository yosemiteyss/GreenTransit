import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.JAVA_LIBRARY)
    id(Plugins.KOTLIN)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.CHECK_DEPENDENCY_UPDATES) version Versions.CHECK_DEPENDENCY_UPDATES
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(project(":test-shared"))

    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.COROUTINE_CORE)
    implementation(Dependencies.DAGGER)
    implementation(Dependencies.PAGING_COMMON)

    kapt(Annotation.DAGGER_COMPILER)

    testImplementation(Dependencies.JUNIT)
    testImplementation(Dependencies.TURBINE)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlin.ExperimentalStdlibApi",
            "-Xopt-in=kotlin.time.ExperimentalTime"
        )
    }
}