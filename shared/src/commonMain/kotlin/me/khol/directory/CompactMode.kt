package me.khol.directory

/**
 * Strategy for merging directory and file names.
 */
enum class CompactMode {
    /**
     * No directories and files will be merged and each will display on a separate line.
     */
    NONE,
    /**
     * Directories and files defined in a single call to [DirectoryScope.directory] or
     * [DirectoryScope.file] will be merged.
     */
    EXACT,
    /**
     * Directories with no sibling directories or files will be merged with their parents.
     */
    DIRECTORIES,
    /**
     * Directories and files with no sibling directories or files will be merged with their parents.
     */
    ALL,
}
