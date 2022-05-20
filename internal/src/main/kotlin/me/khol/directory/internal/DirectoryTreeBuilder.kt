package me.khol.directory.internal

import me.khol.directory.CompactMode
import me.khol.directory.DirectoryContext
import me.khol.directory.DirectoryScope
import me.khol.directory.SortMode

class DirectoryTreeBuilder(
    private val context: DirectoryContext,
    private val name: String,
) : DirectoryScope {

    private val nodes = mutableListOf<Node>()

    override fun file(name: String, vararg names: String) {
        require(name.isNotEmpty()) { "Name of a file must not be empty" }

        val (name, names) = when (context.compactMode) {
            CompactMode.DIRECTORIES -> {
                if (names.isEmpty()) {
                    name to names
                } else {
                    (listOf(name) + names).run {
                        dropLast(1).joinToString("/") to arrayOf(last())
                    }
                }
            }
            CompactMode.ALL,
            CompactMode.NONE -> name to names
            CompactMode.EXACT -> compactMiddlePackagesExact(name, names)
        }

        nodes += if (names.isEmpty()) {
            Node.File(name = name)
        } else {
            DirectoryTreeBuilder(context, name).also {
                it.file(names.first(), *names.drop(1).toTypedArray())
            }.build()
        }
    }

    override fun directory(name: String, vararg names: String, block: DirectoryScope.() -> Unit) {
        require(name.isNotEmpty()) { "Name of a directory must not be empty" }

        val (name, names) = when (context.compactMode) {
            CompactMode.DIRECTORIES,
            CompactMode.ALL,
            CompactMode.NONE -> name to names
            CompactMode.EXACT -> compactMiddlePackagesExact(name, names)
        }

        nodes += if (names.isEmpty()) {
            DirectoryTreeBuilder(context, name).also(block).build()
        } else {
            DirectoryTreeBuilder(context, name).also {
                it.directory(names.first(), *names.drop(1).toTypedArray(), block = block)
            }.build()
        }
    }

    private fun compactMiddlePackagesExact(
        name: String,
        names: Array<out String>,
    ) = (listOf(name) + names).joinToString("/") to emptyArray<String>()


    private inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T =
        if (condition) block() else this

    fun build(): Node {
        val (name, nodes) = (name to nodes as List<Node>)
            // root folder's name is never displayed, skip if `name` is empty
            .runIf(nodes.size == 1 && name.isNotEmpty()) {
                val (parentName, parentNodes) = this
                // EXACT handled in [compactMiddlePackagesExact]
                when (val child = parentNodes.first()) {
                    is Node.File -> when (context.compactMode) {
                        CompactMode.NONE,
                        CompactMode.EXACT,
                        CompactMode.DIRECTORIES -> this
                        CompactMode.ALL -> return Node.File("${parentName}/${child.name}")
                    }
                    is Node.Directory -> when (context.compactMode) {
                        CompactMode.NONE,
                        CompactMode.EXACT -> this
                        CompactMode.DIRECTORIES,
                        CompactMode.ALL -> "${parentName}/${child.name}" to child.nodes
                    }
                }
            }

        return Node.Directory(
            name = name,
            separators = context.separators,
            nodes = nodes
                .run {
                    when (context.sortMode) {
                        SortMode.NONE -> this
                        SortMode.ASC -> sortedBy(Node::name)
                        SortMode.DESC -> sortedByDescending(Node::name)
                    }
                }
                .runIf(context.directoriesFirst) {
                    partition {
                        when (it) { // exhaustive and future-proof
                            is Node.Directory -> true
                            is Node.File -> false
                        }
                    }.let { it.first + it.second }
                }
        )
    }
}
