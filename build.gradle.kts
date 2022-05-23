buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.19.0")
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}
