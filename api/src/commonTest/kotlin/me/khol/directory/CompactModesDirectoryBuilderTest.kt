package me.khol.directory

import kotlin.test.Test
import kotlin.test.assertEquals

class CompactModesDirectoryBuilderTest {

    @Test
    fun directoriesOnly1() = directoriesOnly(CompactMode.NONE)
    @Test
    fun directoriesOnly2() = directoriesOnly(CompactMode.EXACT)
    @Test
    fun directoriesOnly3() = directoriesOnly(CompactMode.DIRECTORIES)
    @Test
    fun directoriesOnly4() = directoriesOnly(CompactMode.ALL)

    private fun directoriesOnly(compactMode: CompactMode) {
        assertEquals(
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
            }.trimIndent(),
            directory(
                DirectoryContext(compactMode = compactMode)
            ) {
                directory("one") {
                    directory("two", "three")
                }
            }
        )
    }

    @Test
    fun fileInDirectory1() = fileInDirectory(CompactMode.NONE)
    @Test
    fun fileInDirectory2() = fileInDirectory(CompactMode.EXACT)
    @Test
    fun fileInDirectory3() = fileInDirectory(CompactMode.DIRECTORIES)
    @Test
    fun fileInDirectory4() = fileInDirectory(CompactMode.ALL)

    private fun fileInDirectory(compactMode: CompactMode) {
        assertEquals(
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
            }.trimIndent(),
            directory(
                DirectoryContext(compactMode = compactMode)
            ) {
                directory("one") {
                    file("two", "three", "file.txt")
                }
            }
        )
    }

    @Test
    fun fileInRootDirectory1() = fileInRootDirectory(CompactMode.NONE)
    @Test
    fun fileInRootDirectory2() = fileInRootDirectory(CompactMode.EXACT)
    @Test
    fun fileInRootDirectory3() = fileInRootDirectory(CompactMode.DIRECTORIES)
    @Test
    fun fileInRootDirectory4() = fileInRootDirectory(CompactMode.ALL)

    private fun fileInRootDirectory(compactMode: CompactMode) {
        assertEquals(
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
            }.trimIndent(),
            directory(
                DirectoryContext(compactMode = compactMode)
            ) {
                file("one", "two", "three", "file.txt")
            }
        )
    }

    @Test
    fun complexHierarchy1() = complexHierarchy(CompactMode.NONE)
    @Test
    fun complexHierarchy2() = complexHierarchy(CompactMode.EXACT)
    @Test
    fun complexHierarchy3() = complexHierarchy(CompactMode.DIRECTORIES)
    @Test
    fun complexHierarchy4() = complexHierarchy(CompactMode.ALL)

    private fun complexHierarchy(compactMode: CompactMode) {
        assertEquals(
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
            }.trimIndent(),
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
        )
    }
}
