// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    kotlin("jvm") version "2.0.21"
//    alias(libs.plugins.compose.compiler)
//    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}