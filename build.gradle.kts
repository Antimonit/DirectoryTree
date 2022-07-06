plugins {
    kotlin("multiplatform") version "1.6.21" apply false
    id("com.vanniktech.maven.publish") version "0.19.0" apply false
    base
    id("jacoco-report-aggregation")
}

dependencies {
    jacocoAggregation(project(":api"))
    jacocoAggregation(project(":internal"))
    jacocoAggregation(project(":shared"))
}

reporting {
    reports {
        @Suppress("UnstableApiUsage")
        register<JacocoCoverageReport>("testCodeCoverageReport") {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
