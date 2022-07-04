package me.khol.directory

typealias Separator = String

data class Separators(
    val empty: Separator = "    ",
    val pipe: Separator = "│   ",
    val split: Separator = "├── ",
    val corner: Separator = "╰── ",
)
