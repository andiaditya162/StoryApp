plugins {
    id("com.android.application") version "8.1.2" apply false
    id ("com.android.library") version "7.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}