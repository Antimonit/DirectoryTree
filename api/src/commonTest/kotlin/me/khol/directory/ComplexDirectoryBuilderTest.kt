package me.khol.directory

import kotlin.test.Test
import kotlin.test.assertEquals

class ComplexDirectoryBuilderTest {
    private val expected =
        """
        ├── featureOne
        │   ╰── build.gradle.kts
        ├── featureTwo
        │   ╰── build.gradle.kts
        ╰── gradle-common
            ├── build.gradle.kts
            ╰── src
                ╰── main
                    ╰── kotlin
                        ╰── com
                            ╰── sample
                                ╰── gradle
                                    ├── android
                                    │   ╰── library.gradle.kts
                                    ├── jacoco
                                    │   ╰── android.gradle.kts
                                    ╰── sonarqube
                                        ╰── android.gradle.kts
        """.trimIndent()

    @Test
    fun withShorthandExpressions() {
        assertEquals(
            expected,
            directory {
                file("featureOne", "build.gradle.kts")
                file("featureTwo", "build.gradle.kts")
                directory("gradle-common") {
                    file("build.gradle.kts")
                    directory("src", "main", "kotlin", "com", "sample", "gradle") {
                        file("android", "library.gradle.kts")
                        file("jacoco", "android.gradle.kts")
                        file("sonarqube", "android.gradle.kts")
                    }
                }
            }
        )
    }

    @Test
    fun withMixedExpressions() {
        assertEquals(
            expected,
            directory {
                directory("featureOne") {
                    file("build.gradle.kts")
                }
                directory("featureTwo") {
                    file("build.gradle.kts")
                }
                directory("gradle-common") {
                    file("build.gradle.kts")
                    directory("src", "main", "kotlin") {
                        directory("com", "sample", "gradle") {
                            file("android", "library.gradle.kts")
                            file("jacoco", "android.gradle.kts")
                            file("sonarqube", "android.gradle.kts")
                        }
                    }
                }
            }
        )
    }

    @Test
    fun withoutShorthandExpressions() {
        assertEquals(
            expected,
            directory {
                directory("featureOne") {
                    file("build.gradle.kts")
                }
                directory("featureTwo") {
                    file("build.gradle.kts")
                }
                directory("gradle-common") {
                    file("build.gradle.kts")
                    directory("src") {
                        directory("main") {
                            directory("kotlin") {
                                directory("com") {
                                    directory("sample") {
                                        directory("gradle") {
                                            directory("android") {
                                                file("library.gradle.kts")
                                            }
                                            directory("jacoco") {
                                                file("android.gradle.kts")
                                            }
                                            directory("sonarqube") {
                                                file("android.gradle.kts")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
