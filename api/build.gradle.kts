plugins {
    kotlin("multiplatform")
    id("java-library")
    jacoco
    id("com.vanniktech.maven.publish")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

mavenPublish {
    sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
}

kotlin {
    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

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
        named("jvmMain")
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
                implementation("io.strikt:strikt-core:0.34.1")
            }
        }
        named("jsMain")
        named("jsTest")
        named("nativeMain")
        named("nativeTest")
    }
}
