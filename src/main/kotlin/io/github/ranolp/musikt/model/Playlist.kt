package io.github.ranolp.musikt.model

import javafx.beans.property.SimpleListProperty
import tornadofx.*

class Playlist {
    private val order: Comparator<Song<*>> = Comparator.comparing<Song<*>, String> { it.title }

    val songsObservable = observableList<Song<*>>()

    val songsProperty = SimpleListProperty<Song<*>>(songsObservable)
    val songs by songsProperty

    fun addSong(song: Song<*>) {
        if (songsObservable.isEmpty()) {
            songsObservable.add(song)
            return
        }

        val midpointIndex = songsObservable.size / 2
        val midpoint = songsObservable.get(midpointIndex)

        val checked = order.compare(midpoint, song)
        val index = when {
            checked < 0 -> {
                songsObservable.indexOfLast {
                    order.compare(it, song) <= 0
                }

            }
            checked > 0 -> {
                songsObservable.indexOfFirst {
                    order.compare(it, song) <= 0
                }
            }
            else -> {
                midpointIndex
            }
        }.let {
            if (it == -1) {
                0
            } else {
                it
            }
        }


        songsObservable.add(index, song)
    }

    fun removeSong(song: Song<*>) {
        songsObservable.remove(song)
    }
}
