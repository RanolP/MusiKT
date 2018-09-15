package io.github.ranolp.musikt.source.soundcloud

import io.github.ranolp.musikt.source.Author
import io.github.ranolp.musikt.source.SourceData


data class SoundCloudData(override val isCached: Boolean, override val title: String, override val authors: Set<Author>
) : SourceData {
    companion object {
        private const val serialVersionUID = -8033810464573230583L
    }
}
