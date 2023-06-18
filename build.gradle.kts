buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0-alpha08")
        classpath("com.google.gms:google-services:4.3.8")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}