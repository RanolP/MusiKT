package io.github.ranolp.musikt.view

import javafx.scene.layout.Priority
import tornadofx.*

class DesktopMusicPlayer : View() {
    override val root = hbox {
        val player = MusicPlayer().root
        val playList = PlayList().root
        addChildIfPossible(player)
        addChildIfPossible(playList)

        playList.managedProperty().bind(playList.visibleProperty())
        playList.hgrow = Priority.ALWAYS

        fun evaluate(width: Double, height: Double) {
            if (width <= 400 || height <= 400) {
                playList.isVisible = false
            } else {
                val ratio = width / height
                playList.isVisible = ratio > 1.5 || (width >= 800 && ratio < 2)
            }

            val playerRatio = player.width / height
            if (playList.isVisible && playerRatio <= 0.9) {
                val newWidth = height * 0.8
                player.minWidth = newWidth
                player.prefWidth = newWidth
                player.maxWidth = newWidth
            } else {
                player.minWidth = Double.NEGATIVE_INFINITY
                player.prefWidth = 0.0
                player.maxWidth = Double.POSITIVE_INFINITY
            }
        }

        playList.visibleProperty().onChange {
            if (it) {
                player.hgrow = Priority.NEVER
            } else {
                player.hgrow = Priority.ALWAYS
            }
        }

        widthProperty().onChange {
            evaluate(width, height)
        }

        heightProperty().onChange {
            evaluate(width, height)
        }
    }

    init {
        title = "MusiKT"
    }
}
