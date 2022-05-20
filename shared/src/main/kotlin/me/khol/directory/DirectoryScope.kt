package me.khol.directory

interface DirectoryScope {

    fun file(name: String, vararg names: String)

    fun directory(name: String, vararg names: String, block: DirectoryScope.() -> Unit = {})
}
