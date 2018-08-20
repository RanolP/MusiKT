package io.github.ranolp.musikt.controller

import io.github.ranolp.musikt.event.SongChangeEvent
import io.github.ranolp.musikt.model.Song
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class MusicPlayerController : Controller() {
    val currentSongProperty = SimpleObjectProperty<Song<*>>()
    var currentSong: Song<*>? by currentSongProperty

    private val _isPlaying = SimpleBooleanProperty()
    val isPlayingProperty = _isPlaying
    var isPlaying by _isPlaying

    var isShuffleModeEnabled = false
        private set
    var isRepeatModeEnabled = false
        private set

    fun startSong(): Boolean {
        val currentSong = currentSong ?: return false
        isPlaying = true
        currentSong.isPlaying = true
        return true
    }

    fun pauseSong() {
        isPlaying = false
        currentSong?.isPlaying = false
    }

    fun enableShuffleMode() {
        isShuffleModeEnabled = true
    }

    fun disableShuffleMode() {
        isShuffleModeEnabled = false
    }

    fun enableRepeatMode() {
        isRepeatModeEnabled = true
    }

    fun disableRepeatMode() {
        isRepeatModeEnabled = false
    }

    init {
        currentSongProperty.addListener { _, old, new ->
                old?.isPlaying = false

                isPlaying = false
                startSong()
        }
        subscribe<SongChangeEvent> {
            if (currentSong == it.newSong) {
                if (isPlaying) {
                    pauseSong()
                } else {
                    startSong()
                }
            }

            currentSong = it.newSong
        }
    }
}
