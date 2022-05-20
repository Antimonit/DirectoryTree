package me.khol.directory

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_WITH_NAMES_PLACEHOLDER
import org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Directory builder")
class DirectoryBuilderTest {

    @Test
    @DisplayName("Empty root directory")
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
    @DisplayName("Single file")
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
    @DisplayName("Multiple files")
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
    @DisplayName("Single directory")
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
    @DisplayName("Multiple directories")
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
    @DisplayName("Nested directories")
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
    @DisplayName("Multiple nested directories")
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
    @DisplayName("Non-uniform directories")
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
    @DisplayName("File then directory")
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
    @DisplayName("Directory then file")
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
    @DisplayName("Shorthand directories")
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
    @DisplayName("Shorthand files")
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

    @Test
    @DisplayName("Custom separators")
    fun customSeparators() {
        expectThat(
            directory(
                DirectoryContext(
                    separators = Separators(
                        empty = "   ",
                        pipe = "║  ",
                        split = "╟─╴",
                        corner = "╙─╴",
                    )
                )
            ) {
                directory("one") {
                    file("file.txt")
                }
                directory("two") {
                    file("file.txt")
                }
            }
        ).isEqualTo(
            """
            ╟─╴one
            ║  ╙─╴file.txt
            ╙─╴two
               ╙─╴file.txt
            """.trimIndent()
        )
    }

    companion object {
        @JvmStatic
        fun sortByNameAndDirectoriesFirst() = arrayOf(
            arrayOf(SortMode.NONE, false),
            arrayOf(SortMode.NONE, true),
            arrayOf(SortMode.ASC, false),
            arrayOf(SortMode.ASC, true),
            arrayOf(SortMode.DESC, false),
            arrayOf(SortMode.DESC, true),
        )
    }

    @ParameterizedTest(name = "$DISPLAY_NAME_PLACEHOLDER ($ARGUMENTS_WITH_NAMES_PLACEHOLDER)")
    @MethodSource
    @DisplayName("Sort by name & directoriesFirst")
    fun sortByNameAndDirectoriesFirst(sortMode: SortMode, directoriesFirst: Boolean) {
        expectThat(
            directory(
                context = DirectoryContext(
                    sortMode = sortMode,
                    directoriesFirst = directoriesFirst,
                )
            ) {
                file("z.txt")
                directory("one") {
                    directory("two") {
                        file("one.txt")
                        file("two.txt")
                        file("three.txt")
                    }
                }
                file("a.txt")
            }
        ).isEqualTo(
            when (directoriesFirst) {
                true -> when (sortMode) {
                    SortMode.NONE -> """
                        ├── one
                        │   ╰── two
                        │       ├── one.txt
                        │       ├── two.txt
                        │       ╰── three.txt
                        ├── z.txt
                        ╰── a.txt
                        """
                    SortMode.ASC -> """
                        ├── one
                        │   ╰── two
                        │       ├── one.txt
                        │       ├── three.txt
                        │       ╰── two.txt
                        ├── a.txt
                        ╰── z.txt
                        """
                    SortMode.DESC -> """
                        ├── one
                        │   ╰── two
                        │       ├── two.txt
                        │       ├── three.txt
                        │       ╰── one.txt
                        ├── z.txt
                        ╰── a.txt
                        """
                }
                false -> when (sortMode) {
                    SortMode.NONE -> """
                        ├── z.txt
                        ├── one
                        │   ╰── two
                        │       ├── one.txt
                        │       ├── two.txt
                        │       ╰── three.txt
                        ╰── a.txt
                        """
                    SortMode.ASC -> """
                        ├── a.txt
                        ├── one
                        │   ╰── two
                        │       ├── one.txt
                        │       ├── three.txt
                        │       ╰── two.txt
                        ╰── z.txt
                        """
                    SortMode.DESC -> """
                        ├── z.txt
                        ├── one
                        │   ╰── two
                        │       ├── two.txt
                        │       ├── three.txt
                        │       ╰── one.txt
                        ╰── a.txt
                        """
                }
            }.trimIndent()
        )
    }

    @Nested
    @DisplayName("Compact modes")
    inner class CompactModes {

        @ParameterizedTest(name = "$DISPLAY_NAME_PLACEHOLDER: $ARGUMENTS_WITH_NAMES_PLACEHOLDER")
        @EnumSource
        @DisplayName("Directories only")
        fun directory(compactMode: CompactMode) {
            expectThat(
                directory(
                    DirectoryContext(compactMode = compactMode)
                ) {
                    directory("one") {
                        directory("two", "three")
                    }
                }
            ).isEqualTo(
                when (compactMode) {
                    CompactMode.NONE -> """
                        ╰── one
                            ╰── two
                                ╰── three
                        """
                    CompactMode.EXACT -> """
                        ╰── one
                            ╰── two/three
                        """
                    CompactMode.DIRECTORIES -> """
                        ╰── one/two/three
                        """
                    CompactMode.ALL -> """
                        ╰── one/two/three
                        """
                }.trimIndent()
            )
        }

        @ParameterizedTest(name = "$DISPLAY_NAME_PLACEHOLDER: $ARGUMENTS_WITH_NAMES_PLACEHOLDER")
        @EnumSource
        @DisplayName("File in a directory")
        fun fileInDirectory(compactMode: CompactMode) {
            expectThat(
                directory(
                    DirectoryContext(compactMode = compactMode)
                ) {
                    directory("one") {
                        file("two", "three", "file.txt")
                    }
                }
            ).isEqualTo(
                when (compactMode) {
                    CompactMode.NONE -> """
                        ╰── one
                            ╰── two
                                ╰── three
                                    ╰── file.txt
                        """
                    CompactMode.EXACT -> """
                        ╰── one
                            ╰── two/three/file.txt
                        """
                    CompactMode.DIRECTORIES -> """
                        ╰── one/two/three
                            ╰── file.txt
                        """
                    CompactMode.ALL -> """
                        ╰── one/two/three/file.txt
                        """
                }.trimIndent()
            )
        }

        @ParameterizedTest(name = "$DISPLAY_NAME_PLACEHOLDER: $ARGUMENTS_WITH_NAMES_PLACEHOLDER")
        @EnumSource
        @DisplayName("File in the root directory")
        fun fileInRoot(compactMode: CompactMode) {
            expectThat(
                directory(
                    DirectoryContext(compactMode = compactMode)
                ) {
                    file("one", "two", "three", "file.txt")
                }
            ).isEqualTo(
                when (compactMode) {
                    CompactMode.NONE -> """
                        ╰── one
                            ╰── two
                                ╰── three
                                    ╰── file.txt
                        """
                    CompactMode.EXACT -> """
                        ╰── one/two/three/file.txt
                        """
                    CompactMode.DIRECTORIES -> """
                        ╰── one/two/three
                            ╰── file.txt
                        """
                    CompactMode.ALL -> """
                        ╰── one/two/three/file.txt
                        """
                }.trimIndent()
            )
        }

        @ParameterizedTest(name = "$DISPLAY_NAME_PLACEHOLDER: $ARGUMENTS_WITH_NAMES_PLACEHOLDER")
        @EnumSource
        @DisplayName("Complex hierarchy")
        fun complex(compactMode: CompactMode) {
            expectThat(
                directory(
                    DirectoryContext(compactMode = compactMode)
                ) {
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
            ).isEqualTo(
                when (compactMode) {
                    CompactMode.NONE -> """
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
                            """
                    CompactMode.EXACT -> """
                            ├── featureOne
                            │   ╰── build.gradle.kts
                            ├── featureTwo
                            │   ╰── build.gradle.kts
                            ╰── gradle-common
                                ├── build.gradle.kts
                                ╰── src/main/kotlin
                                    ╰── com/sample/gradle
                                        ├── android/library.gradle.kts
                                        ├── jacoco/android.gradle.kts
                                        ╰── sonarqube/android.gradle.kts
                            """
                    CompactMode.DIRECTORIES -> """
                            ├── featureOne
                            │   ╰── build.gradle.kts
                            ├── featureTwo
                            │   ╰── build.gradle.kts
                            ╰── gradle-common
                                ├── build.gradle.kts
                                ╰── src/main/kotlin/com/sample/gradle
                                    ├── android
                                    │   ╰── library.gradle.kts
                                    ├── jacoco
                                    │   ╰── android.gradle.kts
                                    ╰── sonarqube
                                        ╰── android.gradle.kts
                            """
                    CompactMode.ALL -> """
                            ├── featureOne/build.gradle.kts
                            ├── featureTwo/build.gradle.kts
                            ╰── gradle-common
                                ├── build.gradle.kts
                                ╰── src/main/kotlin/com/sample/gradle
                                    ├── android/library.gradle.kts
                                    ├── jacoco/android.gradle.kts
                                    ╰── sonarqube/android.gradle.kts
                            """
                }.trimIndent()
            )
        }
    }

    @Nested
    @DisplayName("Complex directories with files")
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
        @DisplayName("With shorthand expressions")
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
        @DisplayName("With mixed expressions")
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
        @DisplayName("Without shorthand expressions")
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
