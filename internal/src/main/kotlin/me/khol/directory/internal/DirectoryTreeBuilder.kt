package me.khol.directory.internal

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
        nodes += if (names.isEmpty()) {
            DirectoryTreeBuilder(context, name).also(block).build()
        } else {
            DirectoryTreeBuilder(context, name).also {
                it.directory(names.first(), *names.drop(1).toTypedArray(), block = block)
            }.build()
        }
    }

    fun build() = Node.Directory(
        name = name,
        nodes = nodes
            .run {
                when (context.sortMode) {
                    SortMode.NONE -> this
                    SortMode.ASC -> sortedBy(Node::name)
                    SortMode.DESC -> sortedByDescending(Node::name)
                }
            }
            .run {
                when (context.directoriesFirst) {
                    true -> partition {
                        when (it) { // exhaustive and future-proof
                            is Node.Directory -> true
                            is Node.File -> false
                        }
                    }.let { it.first + it.second }
                    false -> this
                }
            }
    )
}
