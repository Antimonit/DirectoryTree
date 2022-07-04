package me.khol.directory

import kotlin.test.Test
import kotlin.test.assertEquals

class ElementaryDirectoryBuilderTest {

    @Test
    fun emptyRootDirectory() {
        assertEquals(
            """
            """.trimIndent(),
            directory {
            }
        )
    }

    @Test
    fun singleFile() {
        assertEquals(
            """
            ╰── file.txt
            """.trimIndent(),
            directory {
                file("file.txt")
            }
        )
    }

    @Test
    fun multipleFiles() {
        assertEquals(
            """
            ├── file_one.txt
            ├── file_two.txt
            ╰── file_three.txt
            """.trimIndent(),
            directory {
                file("file_one.txt")
                file("file_two.txt")
                file("file_three.txt")
            }
        )
    }

    @Test
    fun singleDirectory() {
        assertEquals(
            """
            ╰── folder
            """.trimIndent(),
            directory {
                directory("folder")
            }
        )
    }

    @Test
    fun multipleDirectories() {
        assertEquals(
            """
            ├── one
            ├── two
            ╰── three
            """.trimIndent(),
            directory {
                directory("one")
                directory("two")
                directory("three")
            }
        )
    }

    @Test
    fun nestedDirectories() {
        assertEquals(
            """
            ╰── one
                ╰── two
                    ╰── three
            """.trimIndent(),
            directory {
                directory("one") {
                    directory("two") {
                        directory("three")
                    }
                }
            }
        )
    }

    @Test
    fun multipleNestedDirectories() {
        assertEquals(
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
            """.trimIndent(),
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
        )
    }

    @Test
    fun nonUniformDirectories() {
        assertEquals(
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
            """.trimIndent(),
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
        )
    }

    @Test
    fun fileThenDirectory() {
        assertEquals(
            """
            ├── file_one.txt
            ╰── directory
                ╰── file_two.txt
            """.trimIndent(),
            directory {
                file("file_one.txt")
                directory("directory") {
                    file("file_two.txt")
                }
            }
        )
    }

    @Test
    fun directoryThenFile() {
        assertEquals(
            """
            ├── directory
            │   ╰── file_two.txt
            ╰── file_one.txt
            """.trimIndent(),
            directory {
                directory("directory") {
                    file("file_two.txt")
                }
                file("file_one.txt")
            }
        )
    }

    @Test
    fun shorthandDirectories() {
        assertEquals(
            """
            ╰── one
                ╰── two
                    ╰── three
                        ╰── file.txt
            """.trimIndent(),
            directory {
                directory("one", "two", "three") {
                    file("file.txt")
                }
            }
        )
    }

    @Test
    fun shorthandFiles() {
        assertEquals(
            """
            ╰── one
                ╰── two
                    ╰── three
                        ╰── file.txt
            """.trimIndent(),
            directory {
                file("one", "two", "three", "file.txt")
            }
        )
    }

    @Test
    fun customSeparators() {
        assertEquals(
            """
            ╟─╴one
            ║  ╙─╴file.txt
            ╙─╴two
               ╙─╴file.txt
            """.trimIndent(),
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
        )
    }
}
