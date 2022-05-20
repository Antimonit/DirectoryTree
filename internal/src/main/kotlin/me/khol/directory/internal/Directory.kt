package me.khol.directory.internal

sealed interface Node {

    val name: String

    fun toHierarchy(delimiter: Separator, tab: Separator): Hierarchy

    class File(
        override val name: String,
    ) : Node {

        override fun toHierarchy(delimiter: Separator, tab: Separator) = listOf(
            Line(separators = listOf(delimiter), name = name)
        )
    }

    class Directory(
        override val name: String,
        val nodes: List<Node>,
    ) : Node {

        fun toLines(): Hierarchy {
            fun List<Node>.lines(delimiter: Separator, tab: Separator) =
                flatMap { it.toHierarchy(delimiter, tab) }

            val head = nodes.dropLast(1)
            val tail = nodes.takeLast(1)
            return head.lines(Separator.SPLIT, Separator.PIPE) +
                    tail.lines(Separator.CORNER, Separator.EMPTY)
        }

        override fun toHierarchy(delimiter: Separator, tab: Separator) =
            listOf(
                Line(separators = listOf(delimiter), name = name)
            ) + toLines().map { line ->
                line.copy(separators = listOf(tab) + line.separators)
            }
    }
}
