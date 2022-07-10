plugins {
    id("me.khol.gradle.directoryTree.library")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(project(":shared"))
                implementation(project(":internal"))
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
