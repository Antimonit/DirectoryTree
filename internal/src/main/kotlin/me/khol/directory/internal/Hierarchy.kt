package me.khol.directory.internal

typealias Hierarchy = List<Line>

data class Line(
    val separators: List<Separator>,
    val name: String,
)
