package me.khol.directory.internal

import me.khol.directory.DirectoryScope

class DirectoryTreeBuilder(
    private val name: String,
) : DirectoryScope {
    private val nodes = mutableListOf<Node>()

    override fun file(name: String, vararg names: String) {
        nodes += if (names.isEmpty()) {
            Node.File(name = name)
        } else {
            DirectoryTreeBuilder(name).also {
                it.file(names.first(), *names.drop(1).toTypedArray())
            }.build()
        }
    }

    override fun directory(name: String, vararg names: String, block: DirectoryScope.() -> Unit) {
        nodes += if (names.isEmpty()) {
            DirectoryTreeBuilder(name).also(block).build()
        } else {
            DirectoryTreeBuilder(name).also {
                it.directory(names.first(), *names.drop(1).toTypedArray(), block = block)
            }.build()
        }
    }

    fun build() = Node.Directory(name = name, nodes = nodes)
}
