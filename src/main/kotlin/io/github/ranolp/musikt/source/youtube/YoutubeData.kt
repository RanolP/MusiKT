package io.github.ranolp.musikt.source.youtube

import io.github.ranolp.musikt.source.Author
import io.github.ranolp.musikt.source.SourceData

data class YoutubeData(override val isCached: Boolean, override val title: String, override val authors: Set<Author>) :
        SourceData {
    companion object {
        private const val serialVersionUID = 6180167409455284352L
    }
}
