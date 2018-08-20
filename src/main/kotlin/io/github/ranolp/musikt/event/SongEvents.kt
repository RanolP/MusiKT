package io.github.ranolp.musikt.event

import io.github.ranolp.musikt.model.Song
import tornadofx.*

data class SongChangeEvent(val newSong: Song<*>) : FXEvent()
