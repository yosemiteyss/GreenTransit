plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.HILT_ANDROID)
    id(Plugins.GOOGLE_SERVICES)
    id(Plugins.CHECK_DEPENDENCY_UPDATES) version Versions.CHECK_DEPENDENCY_UPDATES
}

android {
    compileSdkVersion(Application.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(Application.MIN_SDK)
        targetSdkVersion(Application.TARGET_SDK)
        versionCode = Application.MAIN_VERSION_CODE
        versionName = Application.MAIN_VERSION_NAME
        testInstrumentationRunner = "com.yosemiteyss.greentransit.app.HiltTestRunner"

        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
                "-Xuse-experimental=androidx.paging.ExperimentalPagingApi"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":domain"))

    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.COROUTINE_CORE)
    implementation(Dependencies.COROUTINE_ANDROID)
    implementation(Dependencies.COROUTINE_PLAY_SERVICES)
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.HILT)
    implementation(Dependencies.PAGING_RUNTIME)
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_CONVERTER_GSON)
    implementation(Dependencies.RETROFIT_CONVERTER_XML)
    implementation(Dependencies.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Dependencies.PLAY_SERVICES_MAP)
    implementation(Dependencies.MAPS_UTILS)

    api(Dependencies.FIREBASE_FIRESTORE)
    api(Dependencies.ROOM_RUNTIME)
    api(Dependencies.ROOM_KTX)

    // Annotation Processors
    kapt(Annotation.ROOM_COMPILER)
    kapt(Annotation.HILT_ANDROID_COMPILER)

    testImplementation(Dependencies.JUNIT)
}