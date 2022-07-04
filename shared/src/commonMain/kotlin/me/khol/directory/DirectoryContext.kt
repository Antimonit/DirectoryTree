package me.khol.directory

class DirectoryContext(

    /**
     * Style of "pipes" displayed in the output.
     */
    val separators: Separators = Separators(),

    /**
     * Sort files and directories by their names.
     */
    val sortMode: SortMode = SortMode.NONE,

    /**
     * Reorder contents of all directories to put directories before files.
     */
    val directoriesFirst: Boolean = false,

    /**
     * Strategy for merging directory and file names.
     *
     * See [CompactMode] for details.
     */
    val compactMode: CompactMode = CompactMode.NONE,
)
