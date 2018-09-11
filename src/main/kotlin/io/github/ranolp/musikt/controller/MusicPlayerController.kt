package io.github.ranolp.musikt.controller

import io.github.ranolp.musikt.event.SongChangeEvent
import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.source.Source
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.util.concurrent.CompletableFuture
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import kotlin.concurrent.timer
import kotlin.math.round

class MusicPlayerController : Controller() {
    val currentSongProperty = SimpleObjectProperty<Song<*>>()
    var currentSong: Song<*>? by currentSongProperty

    val isPlayingProperty = SimpleBooleanProperty()
    var isPlaying by isPlayingProperty

    val isLoadingProperty = SimpleBooleanProperty()
    var isLoading by isLoadingProperty

    var isShuffleModeEnabled = false
        private set
    var isRepeatModeEnabled = false
        private set

    private lateinit var clip: Clip

    val currentProperty = SimpleIntegerProperty()
    var current by currentProperty

    val maxProperty = SimpleIntegerProperty()
    var max by maxProperty

    fun startSong(): Boolean {
        val currentSong = currentSong ?: return false
        CompletableFuture.runAsync {
            clip = AudioSystem.getClip()
            if (currentSong.cache == null) {
                currentSong.isLoading = true
                isLoading = true
                Platform.runLater {
                    currentSong.isLoading = true
                }
                currentSong.cache = currentSong.source.get { percentage ->
                    Platform.runLater {
                        currentSong.loadingProgressProperty.set(percentage.toInt())
                    }
                }
                isLoading = false
                Platform.runLater {
                    currentSong.isLoading = false
                }
            }

            currentSong.cache?.firstOrNull {
                it.type == Source.Type.WAV
            }?.let {
                clip.open(AudioSystem.getAudioInputStream(it.path.toFile()))
            }

            if (current > 0) {
                clip.framePosition = (current * clip.format.frameRate).toInt()
            }
            clip.start()
            val frameRate = clip.format.frameRate
            Platform.runLater {
                max = round(clip.frameLength / frameRate).toInt()
            }
            timer(period = 1000) {
                if(!clip.isOpen) {
                    cancel()
                    return@timer
                }
                Platform.runLater {
                    current = round(clip.framePosition / frameRate).toInt()
                }
            }
            while (clip.isRunning) {
            }
        }.exceptionally {
            throw it
        }
        isPlaying = true
        currentSong.isPlaying = true
        return true
    }

    fun pauseSong() {
        if (::clip.isInitialized && clip.isOpen) {
            clip.close()
        }
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
            } else {
                pauseSong()
                current = 0
                max = 0
            }

            currentSong = it.newSong
        }
    }
}
