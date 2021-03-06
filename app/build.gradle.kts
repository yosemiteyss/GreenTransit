import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id(Plugins.ANDROID_APPLICATION)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_PARCELIZE)
    id(Plugins.HILT_ANDROID)
    id(Plugins.NAVIGATION_SAFEARGS)
    id(Plugins.FIREBASE_CRASHLYTICS)
    id(Plugins.CHECK_DEPENDENCY_UPDATES) version Versions.CHECK_DEPENDENCY_UPDATES
}

android {
    val localProperties = Properties()
    localProperties.load(rootProject.file("local.properties").inputStream())

    compileSdkVersion(Application.COMPILE_SDK)

    defaultConfig {
        applicationId = Application.MAIN_APPLICATION_ID
        minSdkVersion(Application.MIN_SDK)
        targetSdkVersion(Application.TARGET_SDK)
        versionCode = Application.MAIN_VERSION_CODE
        versionName = Application.MAIN_VERSION_NAME
        testInstrumentationRunner = "com.yosemiteyss.greentransit.app.HiltTestRunner"
    }

    buildTypes {
        // Release build
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        // Debug build
        getByName("debug") {
            addManifestPlaceholders(
                mapOf("mapsApiKey" to localProperties.getProperty("GCLOUD_API", null))
            )
        }
    }

    /*
    sourceSets {
        // Provide fake repositories
        getByName("test") {
            java.srcDir(project(":domain").file("src/test/java"))
        }

        getByName("androidTest") {
            java.srcDir(project(":domain").file("src/test/java"))
        }
    }

     */

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
        }
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("**/attach_hotspot_windows.dll")
        exclude("META-INF/licenses/**")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":data"))
    implementation(project(":domain"))
    testImplementation(project(":test-shared"))
    androidTestImplementation(project(":test-shared"))

    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.COROUTINE_CORE)
    implementation(Dependencies.COROUTINE_ANDROID)
    implementation(Dependencies.COROUTINE_PLAY_SERVICES)
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.CONSTRAINT_LAYOUT)
    implementation(Dependencies.ACTIVITY)
    implementation(Dependencies.FRAGMENT)
    implementation(Dependencies.NAVIGATION_FRAGMENT)
    implementation(Dependencies.NAVIGATION_UI)
    implementation(Dependencies.LIFECYCLE_RUNTIME)
    implementation(Dependencies.LIFECYCLE_LIVEDATA)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL_SAVEDSTATE)
    implementation(Dependencies.LIFECYCLE_COMMON)
    implementation(Dependencies.HILT)
    implementation(Dependencies.HILT_NAVIGATION)
    implementation(Dependencies.HILT_WORK)
    implementation(Dependencies.SWIPE_REFRESH_LAYOUT)
    implementation(Dependencies.PLAY_SERVICES_MAP)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.GLIDE)
    implementation(Dependencies.MAPS_KTX)
    implementation(Dependencies.MAPS_UTILS)
    implementation(Dependencies.WORK)
    implementation(Dependencies.FIREBASE_ANALYTICS)
    implementation(Dependencies.FIREBASE_CRASHLYTICS)
    implementation(Dependencies.FIREBASE_GEOFIRE)

    // Annotation Processors
    kapt(Annotation.HILT_ANDROID_COMPILER)
    kapt(Annotation.HILT_ANDROIDX_EXT_COMPILER)

    // Unit Test
    testImplementation(Dependencies.ARCH_CORE_TESTING)
    testImplementation(Dependencies.JUNIT)
    testImplementation(Dependencies.MOCKK)
    testImplementation(Dependencies.TURBINE)
    testImplementation(Dependencies.TEST_CORE)

    // Android Test
    androidTestImplementation(Dependencies.HILT_TESTING)
    //androidTestImplementation(Dependencies.TEST_ESPRESSO_CORE)
    //androidTestImplementation(Dependencies.TEST_ESPRESSO_CONTRIB)
    androidTestImplementation(Dependencies.TEST_RUNNER)
    androidTestImplementation(Dependencies.TEST_EXT_JUNIT_KTX)
    androidTestImplementation(Dependencies.NAVIGATION_TESTING)
    androidTestImplementation(Dependencies.ARCH_CORE_TESTING)
    androidTestImplementation(Dependencies.BARISTA) {
        exclude("org.jetbrains.kotlin")
    }

    debugImplementation(Dependencies.FRAGMENT_TESTING)
    //debugImplementation(Dependencies.LEAK_CANARY)

    kaptAndroidTest(Annotation.HILT_ANDROID_COMPILER)
}