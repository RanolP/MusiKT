package io.github.ranolp.musikt.controller

import tornadofx.*

class MusicPlayerController : Controller() {

    var isPlaying = false
        private set
    var isShuffleModeEnabled = false
        private set
    var isRepeatModeEnabled = false
        private set

    fun startSong() {
        isPlaying = true
    }

    fun pauseSong() {
        isPlaying = false
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
}
