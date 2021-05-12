object Dependencies {
    // Gradle Plugins
    const val GRADLE_PLUGIN = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val KOTLIN_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val NAVIGATION_SAFE_ARGS_PLUGIN = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
    const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Versions.GOOGLE_SERVICES}"
    const val FIREBASE_CRASHLYTICS_PLUGIN = "com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS_GRADLE}"

    // Kotlin
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
    const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION_JSON}"
    const val COROUTINE_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}"
    const val COROUTINE_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}"
    const val COROUTINE_PLAY_SERVICES = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.COROUTINE}"

    // Android
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.ACTIVITY}"
    const val BROWSER = "androidx.browser:browser:${Versions.BROWSER}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_VIEWMODEL_SAVEDSTATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.LIFECYCLE}"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_COMMON = "androidx.lifecycle:lifecycle-common-java8:${Versions.LIFECYCLE}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val HILT = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_NAVIGATION = "androidx.hilt:hilt-navigation-fragment:${Versions.HILT_NAVIGATION}"
    const val HILT_WORK = "androidx.hilt:hilt-work:${Versions.HILT_WORK}"
    const val PAGING_RUNTIME = "androidx.paging:paging-runtime-ktx:${Versions.PAGING}"
    const val PAGING_COMMON = "androidx.paging:paging-common-ktx:${Versions.PAGING}"
    const val PREFERENCES = "androidx.preference:preference-ktx:${Versions.PREFERENCES}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"
    const val WORK = "androidx.work:work-runtime-ktx:${Versions.WORK}"
    const val SWIPE_REFRESH_LAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE_REFRESH_LAYOUT}"

    // Firebase
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx:${Versions.FIREBASE_AUTH}"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx:${Versions.FIREBASE_ANALYTICS}"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx:${Versions.FIREBASE_CRASHLTCICS}"
    const val FIREBASE_DYNAMIC_LINKS = "com.google.firebase:firebase-dynamic-links-ktx:${Versions.FIREBASE_DYNAMIC_LINKS}"
    const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore-ktx:${Versions.FIREBASE_FIRESTORE}"
    const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging:${Versions.FIREBASE_MESSAGING}"
    const val FIREBASE_STORAGE = "com.google.firebase:firebase-storage-ktx:${Versions.FIREBASE_STORAGE}"
    const val FIREBASE_GEOFIRE = "com.firebase:geofire-android-common:${Versions.FIREBASE_GEOFIRE}"

    // Play Services
    const val PLAY_SERVICES_AUTH = "com.google.android.gms:play-services-auth:${Versions.PLAY_SERVICES_AUTH}"
    const val PLAY_SERVICES_MAP = "com.google.android.gms:play-services-maps:${Versions.PLAY_SERVICES_MAP}"
    const val PLAY_SERVICES_LOCATION = "com.google.android.gms:play-services-location:${Versions.PLAY_SERVICES_LOCATION}"

    // Others
    const val BANNER_VIEW_PAGER = "com.github.zhpanvip:BannerViewPager:${Versions.BANNER_VIEW_PAGER}"
    const val DAGGER = "com.google.dagger:dagger:${Versions.DAGGER}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val SPINKIT = "com.github.ybq:Android-SpinKit:${Versions.SPINKIT}"
    const val MAPS_KTX = "com.google.maps.android:maps-ktx:${Versions.MAPS_KTX}"
    const val MAPS_UTILS = "com.google.maps.android:android-maps-utils:${Versions.MAPS_UTILS}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER_XML = "com.squareup.retrofit2:converter-simplexml:${Versions.RETROFIT}"
    const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP_LOGGING_INTERCEPTOR}"
    const val ZXING_CORE = "com.google.zxing:core:${Versions.ZXING_CORE}"
    const val ZXING_EMBEDDED = "com.journeyapps:zxing-android-embedded:${Versions.ZXING_EMBEDDED}"
    const val LICENSE = "com.jaredsburrows:gradle-license-plugin:0.8.90"

    // Test
    const val ARCH_CORE_TESTING = "androidx.arch.core:core-testing:${Versions.ARCH}"
    const val COROUTINE_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINE}"
    const val ANDROID_JUNIT_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    const val HILT_TEST_RUNNER = "com.foobarust.android.HiltTestRunner"
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val KOTLIN_TEST = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN}"
    const val MOCKITO_CORE = "org.mockito:mockito-core:${Versions.MOCKITO_CORE}"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val TURBINE = "app.cash.turbine:turbine:${Versions.TURBINE}"

    // Android Test
    const val TEST_CORE = "androidx.test:core:${Versions.TEST_CORE}"
    const val TEST_RUNNER = "androidx.test:rules:${Versions.TEST_RUNNER}"
    const val TEST_RULES = "androidx.test:rules:${Versions.TEST_RULES}"
    const val TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.TEST_EXT_JUNIT}"
    const val TEST_EXT_JUNIT_KTX = "androidx.test.ext:junit-ktx:${Versions.TEST_EXT_JUNIT}"
    const val TEST_ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.TEST_ESPRESSO_CORE}"
    const val FRAGMENT_TESTING = "androidx.fragment:fragment-testing:${Versions.FRAGMENT}"
    const val NAVIGATION_TESTING = "androidx.navigation:navigation-testing:${Versions.NAVIGATION}"
    const val ROOM_TESTING = "androidx.room:room-testing:${Versions.ROOM}"
    const val HILT_TESTING = "com.google.dagger:hilt-android-testing:${Versions.HILT}"
}