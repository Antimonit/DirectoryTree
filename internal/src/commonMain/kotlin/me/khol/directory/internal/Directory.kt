package me.khol.directory.internal

import me.khol.directory.Separator
import me.khol.directory.Separators

sealed interface Node {

    val name: String

    fun toLines(): Hierarchy

    fun toHierarchy(delimiter: Separator, tab: Separator): Hierarchy

    class File(
        override val name: String,
    ) : Node {

        override fun toLines(): Hierarchy = listOf(
            Line(separators = emptyList(), name = name)
        )

        override fun toHierarchy(delimiter: Separator, tab: Separator) = toLines().map { line ->
            line.copy(separators = listOf(delimiter) + line.separators)
        }
    }

    class Directory(
        override val name: String,
        private val separators: Separators,
        val nodes: List<Node>,
    ) : Node {

        override fun toLines(): Hierarchy {
            fun List<Node>.lines(delimiter: Separator, tab: Separator) =
                flatMap { it.toHierarchy(delimiter, tab) }

            val head = nodes.dropLast(1)
            val tail = nodes.takeLast(1)
            return head.lines(separators.split, separators.pipe) +
                    tail.lines(separators.corner, separators.empty)
        }

        override fun toHierarchy(delimiter: Separator, tab: Separator) =
            listOf(
                Line(separators = listOf(delimiter), name = name)
            ) + toLines().map { line ->
                line.copy(separators = listOf(tab) + line.separators)
            }
    }
}
