plugins {
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
            reportTask.configure {
                val aggregateTask = this
                subprojects {
                    tasks.withType<JacocoReport>().configureEach {
                        val subTask = this
                        aggregateTask.sourceDirectories.from(subTask.sourceDirectories)
                        aggregateTask.classDirectories.from(subTask.classDirectories)
                        aggregateTask.executionData.from(subTask.executionData)
                    }
                }

                reports {
                    html.required.set(true)
                    csv.required.set(false)
                    xml.required.set(true)
                }
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
