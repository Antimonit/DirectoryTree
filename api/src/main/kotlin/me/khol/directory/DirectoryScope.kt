package me.khol.directory

import me.khol.directory.internal.DirectoryTreeBuilder
import me.khol.directory.internal.Separator

fun directory(block: DirectoryScope.() -> Unit): String =
    DirectoryTreeBuilder("root").also(block).build()
        .toLines()
        .joinToString("\n") { line ->
            line.separators.joinToString(
                separator = "",
                transform = Separator::text,
            ) + line.name
        }
