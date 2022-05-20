package me.khol.directory.internal

enum class Separator(val text: String) {
    EMPTY("    "),
    PIPE("│   "),
    SPLIT("├── "),
    CORNER("╰── "),
}
