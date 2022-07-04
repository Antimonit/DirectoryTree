package me.khol.directory

import me.khol.directory.internal.DirectoryTreeBuilder

fun directory(
    context: DirectoryContext = DirectoryContext(),
    block: DirectoryScope.() -> Unit,
): String = DirectoryTreeBuilder(context = context, name = "")
    .also(block)
    .build()
    .toLines()
    .joinToString("\n") { line ->
        line.separators.joinToString("") + line.name
    }
