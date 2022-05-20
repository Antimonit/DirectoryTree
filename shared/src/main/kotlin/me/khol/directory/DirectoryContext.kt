package me.khol.directory

class DirectoryContext(
    val separators: Separators = Separators(),
    val sortMode: SortMode = SortMode.NONE,
    val directoriesFirst: Boolean = false,
    val compactMode: CompactMode = CompactMode.NONE,
)
