package me.khol.directory

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("directory builder")
class DirectoryBuilderTest {

    @Test
    @DisplayName("empty root directory")
    fun emptyRootDirectory() {
        expectThat(
            directory {
            }
        ).isEqualTo(
            """
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("single file")
    fun singleFile() {
        expectThat(
            directory {
                file("file.txt")
            }
        ).isEqualTo(
            """
            ╰── file.txt
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("multiple files")
    fun multipleFiles() {
        expectThat(
            directory {
                file("file_one.txt")
                file("file_two.txt")
                file("file_three.txt")
            }
        ).isEqualTo(
            """
            ├── file_one.txt
            ├── file_two.txt
            ╰── file_three.txt
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("single directory")
    fun singleDirectory() {
        expectThat(
            directory {
                directory("folder")
            }
        ).isEqualTo(
            """
            ╰── folder
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("multiple directories")
    fun multipleDirectories() {
        expectThat(
            directory {
                directory("one")
                directory("two")
                directory("three")
            }
        ).isEqualTo(
            """
            ├── one
            ├── two
            ╰── three
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("nested directories")
    fun nestedDirectories() {
        expectThat(
            directory {
                directory("one") {
                    directory("two") {
                        directory("three")
                    }
                }
            }
        ).isEqualTo(
            """
            ╰── one
                ╰── two
                    ╰── three
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("multiple nested directories")
    fun multipleNestedDirectories() {
        expectThat(
            directory {
                directory("one") {
                    directory("one_one") {
                        directory("one_one_one")
                        directory("one_one_two")
                    }
                    directory("one_two") {
                        directory("one_two_one")
                        directory("one_two_two")
                    }
                }
                directory("two") {
                    directory("two_one") {
                        directory("two_one_one")
                        directory("two_one_two")
                    }
                    directory("two_two") {
                        directory("two_two_one")
                        directory("two_two_two")
                    }
                }
            }
        ).isEqualTo(
            """
            ├── one
            │   ├── one_one
            │   │   ├── one_one_one
            │   │   ╰── one_one_two
            │   ╰── one_two
            │       ├── one_two_one
            │       ╰── one_two_two
            ╰── two
                ├── two_one
                │   ├── two_one_one
                │   ╰── two_one_two
                ╰── two_two
                    ├── two_two_one
                    ╰── two_two_two
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("non-uniform directories")
    fun nonUniformDirectories() {
        expectThat(
            directory {
                directory("one") {
                    directory("one_one")
                    directory("one_two") {
                        directory("one_two_one")
                        directory("one_two_two")
                    }
                    directory("one_three") {
                        directory("one_three_one")
                    }
                }
                directory("two") {
                    directory("two_one") {
                        directory("two_one_one")
                    }
                }
            }
        ).isEqualTo(
            """
            ├── one
            │   ├── one_one
            │   ├── one_two
            │   │   ├── one_two_one
            │   │   ╰── one_two_two
            │   ╰── one_three
            │       ╰── one_three_one
            ╰── two
                ╰── two_one
                    ╰── two_one_one
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("file then directory")
    fun fileThenDirectory() {
        expectThat(
            directory {
                file("file_one.txt")
                directory("directory") {
                    file("file_two.txt")
                }
            }
        ).isEqualTo(
            """
            ├── file_one.txt
            ╰── directory
                ╰── file_two.txt
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("directory then file")
    fun directoryThenFile() {
        expectThat(
            directory {
                directory("directory") {
                    file("file_two.txt")
                }
                file("file_one.txt")
            }
        ).isEqualTo(
            """
            ├── directory
            │   ╰── file_two.txt
            ╰── file_one.txt
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("shorthand directories")
    fun shorthandDirectories() {
        expectThat(
            directory {
                directory("one", "two", "three") {
                    file("file.txt")
                }
            }
        ).isEqualTo(
            """
            ╰── one
                ╰── two
                    ╰── three
                        ╰── file.txt
            """.trimIndent()
        )
    }

    @Test
    @DisplayName("shorthand files")
    fun shorthandFiles() {
        expectThat(
            directory {
                file("one", "two", "three", "file.txt")
            }
        ).isEqualTo(
            """
            ╰── one
                ╰── two
                    ╰── three
                        ╰── file.txt
            """.trimIndent()
        )
    }

    @Nested
    @DisplayName("complex directories with files")
    inner class ComplexDirectories {
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
        @DisplayName("with shorthand expressions")
        fun withShorthandExpressions() {
            expectThat(
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
            ).isEqualTo(expected)
        }

        @Test
        @DisplayName("with mixed expressions")
        fun withMixedExpressions() {
            expectThat(
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
            ).isEqualTo(expected)
        }

        @Test
        @DisplayName("without shorthand expressions")
        fun withoutShorthandExpressions() {
            expectThat(
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
            ).isEqualTo(expected)
        }
    }
}
