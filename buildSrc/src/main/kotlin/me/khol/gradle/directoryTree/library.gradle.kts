package me.khol.gradle.directoryTree

plugins {
    kotlin("multiplatform")
    id("java-library")
    jacoco
    id("com.vanniktech.maven.publish")
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
        named("commonMain")
        named("commonTest")
        named("jvmMain")
        named("jvmTest")
        named("jsMain")
        named("jsTest")
        named("nativeMain")
        named("nativeTest")
    }
}

//tasks.named("jvmTest") {
//    // This configuration is independent of the `jacocoTaskReport` task below.
//    extensions.configure(JacocoTaskExtension::class) {
//        // Jacoco plugin by default names the destination file the same as the Test task. For
//        // Kotlin Multiplatform that is `jvmTest.exec`. Unfortunately, the Jacoco aggregation
//        // plugin looks for `test.exec` file by default, and I don't know how to change it.
//        setDestinationFile(File("$buildDir/jacoco/test.exec"))
//    }
//}

// report is always generated after tests run
tasks.named("jvmTest") {
    finalizedBy(tasks.jacocoTestReport)
}

// tests are required to run before generating the report
tasks.jacocoTestReport {
    dependsOn(tasks.named("jvmTest"))
}

tasks.jacocoTestReport {
    /*
     * `sourceSets.map { it.java }` - returns only java source sets for jvmMain and jvmTest, no kotlin
     * `sourceSets.map { it.allJava }` - same as above
     * `sourceSets.map { it.allSource }` - only java and resources source sets, no kotlin
     * `kotlin.sourceSets.map { it.kotlin } ` - includes only kotlin source set for commonMain and
     *   commonTest but both kotlin and java sources for jvmMain and jvmTest
    */
    sourceDirectories.setFrom(
        kotlin.sourceSets["jvmMain"].let { jvmMain ->
            (jvmMain.dependsOn + jvmMain).map { it.kotlin.sourceDirectories }
        }
    )
    classDirectories.setFrom(
        sourceSets["main"].output.classesDirs
    )

    executionData(File(buildDir, "jacoco/jvmTest.exec"))
    reports {
        xml.required.set(true)
    }
}
