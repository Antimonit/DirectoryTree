package me.khol.directory.internal

import me.khol.directory.DirectoryContext
import me.khol.directory.DirectoryScope

class DirectoryTreeBuilder(
    private val context: DirectoryContext,
    private val name: String,
) : DirectoryScope {

    private val nodes = mutableListOf<Node>()

    override fun file(name: String, vararg names: String) {
        nodes += if (names.isEmpty()) {
            Node.File(name = name)
        } else {
            DirectoryTreeBuilder(context, name).also {
                it.file(names.first(), *names.drop(1).toTypedArray())
            }.build()
        }
    }

    override fun directory(name: String, vararg names: String, block: DirectoryScope.() -> Unit) {
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
        nodes = nodes.run {
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
