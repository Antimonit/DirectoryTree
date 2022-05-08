package me.khol.directory.internal

import me.khol.directory.DirectoryScope

internal class DirectoryTreeBuilder internal constructor(
    private val name: String,
) : DirectoryScope {
    private val nodes = mutableListOf<Node>()

    override fun file(name: String) {
        nodes += Node.File(name = name)
    }

    override fun directory(name: String, block: DirectoryScope.() -> Unit) {
        nodes += DirectoryTreeBuilder(name).also(block).build()
    }

    internal fun build() = Node.Directory(name = name, nodes = nodes)
}
