// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {



    repositories {
        google()
        mavenCentral()
    }

//    kotlin_version ("1.3.10")
    dependencies {
        // Hilt Gradle Plugin for dependency injection
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath("com.android.tools.build:gradle:8.2.1")

    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.google.gms.google-services") version "4.3.14" apply false

}