package me.khol.directory.internal

internal typealias Hierarchy = List<Line>

internal data class Line(
    val separators: List<Separator>,
    val name: String,
)
