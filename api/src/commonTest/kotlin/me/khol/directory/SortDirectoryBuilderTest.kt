package me.khol.directory

import kotlin.test.Test
import kotlin.test.assertEquals

class SortDirectoryBuilderTest {

    @Test
    fun sort1() = sortByNameAndDirectoriesFirst(SortMode.NONE, false)
    @Test
    fun sort2() = sortByNameAndDirectoriesFirst(SortMode.NONE, true)
    @Test
    fun sort3() = sortByNameAndDirectoriesFirst(SortMode.ASC, false)
    @Test
    fun sort4() = sortByNameAndDirectoriesFirst(SortMode.ASC, true)
    @Test
    fun sort5() = sortByNameAndDirectoriesFirst(SortMode.DESC, false)
    @Test
    fun sort6() = sortByNameAndDirectoriesFirst(SortMode.DESC, true)

    private fun sortByNameAndDirectoriesFirst(sortMode: SortMode, directoriesFirst: Boolean) {
        assertEquals(
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
            },
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
}
