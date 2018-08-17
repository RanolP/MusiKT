package io.github.ranolp.musikt.source

import java.io.Serializable

data class Author(val name: String, val connections: Set<Connection>) : Serializable {
    companion object {
        private const val serialVersionUID = 979801902093430180L
        val Unknown = Author("Unknown", emptySet())
    }
}
