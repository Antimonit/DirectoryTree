plugins {
    id("me.khol.gradle.directoryTree.library")
}

kotlin {
    sourceSets {
        named("commonMain")
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
