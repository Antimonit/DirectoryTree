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
            testTask {
                useKarma {
                    useFirefox()
                    useChrome()
                    useSafari()
                }
            }
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
        named("commonMain")
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("jvmMain")
        named("jvmTest")
        named("jsMain")
        named("jsTest")
        named("nativeMain")
        named("nativeTest")
    }
}
