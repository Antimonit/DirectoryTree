package me.khol.directory.internal

import me.khol.directory.Separator

typealias Hierarchy = List<Line>

data class Line(
    val separators: List<Separator>,
    val name: String,
)
