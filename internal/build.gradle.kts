plugins {
    id("me.khol.gradle.directoryTree.library")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(project(":shared"))
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
