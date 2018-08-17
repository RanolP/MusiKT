package io.github.ranolp.musikt.source

import java.io.Serializable

interface SourceData : Serializable {
    val title: String
    val authors: Set<Author>
    val isCached: Boolean

}
