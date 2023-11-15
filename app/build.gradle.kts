plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.dityapra.mystoryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dityapra.mystoryapp"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        buildConfigField ("String", "API_URL", "\"https://story-api.dicoding.dev/v1/\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        freeCompilerArgs += ("-opt-in=kotlin.RequiresOptIn")
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
    packagingOptions {
        resources {
            excludes += ("META-INF/AL2.0', 'META-INF/LGPL2.1")
        }
    }
    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation ("com.google.android.material:material:1.10.0")

    implementation ("androidx.annotation:annotation:1.7.0")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //testing
    testImplementation ("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test:core-ktx:1.5.0")

    implementation ("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation ("com.android.support.test.espresso:espresso-core:3.0.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation ("com.android.support.test.espresso:espresso-contrib:3.0.2")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")

    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation ("org.mockito:mockito-core:5.6.0")
    testImplementation("org.mockito:mockito-inline:3.12.4")

    androidTestImplementation ("com.squareup.okhttp3:mockwebserver:4.9.3")
    androidTestImplementation ("com.squareup.okhttp3:okhttp-tls:4.9.3")

    implementation ("androidx.core:core-splashscreen:1.1.0-alpha02")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("androidx.camera:camera-camera2:1.4.0-alpha02")
    implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha02")
    implementation ("androidx.camera:camera-view:1.4.0-alpha02")

    implementation ("androidx.room:room-ktx:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-paging:2.6.0")
    implementation ("androidx.paging:paging-runtime-ktx:3.1.1")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    testImplementation ("org.mockito:mockito-core:5.6.0")
    testImplementation ("org.mockito:mockito-inline:3.12.4")

}