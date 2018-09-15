package io.github.ranolp.musikt.view

import io.github.ranolp.musikt.event.SongChangeEvent
import io.github.ranolp.musikt.model.Playlist
import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.util.convertTo
import io.github.ranolp.musikt.util.javafx.circularProgressBar
import io.github.ranolp.musikt.util.javafx.customDialogView
import io.github.ranolp.musikt.util.javafx.icon
import io.github.ranolp.musikt.util.javafx.margin
import io.github.ranolp.musikt.util.javafx.size
import io.github.ranolp.musikt.util.parseJson
import io.github.ranolp.musikt.util.saveInto
import io.github.ranolp.musikt.util.serializeJson
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.OverrunStyle
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import org.kordamp.ikonli.feather.Feather
import org.kordamp.ikonli.ionicons4.Ionicons4IOS
import org.kordamp.ikonli.ionicons4.Ionicons4Material
import tornadofx.*
import java.nio.file.Paths

class PlayList : View() {
    val playlistPath = Paths.get("playlist/index.json")
    val playlist = playlistPath.parseJson()?.convertTo<Playlist>() ?: Playlist()

    override val root = vbox {
        addClass(Style.realRoot)
        listview<Song<*>> {
            setOnMouseMoved {
                scene.onMouseMoved.handle(it)
            }
            vboxConstraints {
                vgrow = Priority.ALWAYS
            }
            onMouseMovedProperty().onChange {
                println(it)
            }
            fixedCellSize = 64.0
            cellFormat { song ->
                graphic = cache(song) {
                    hbox(spacing = 8.0) {
                        alignment = Pos.CENTER_LEFT
                        minHeight = 64.0
                        circularProgressBar {
                            size(16)
                            strokeWidth = 2.0
                            strokeColor = Color.WHITE
                            hide()
                            song.loadingProgressProperty.onChange {
                                progress = it / 100.0
                            }
                            song.isLoadingProperty.onChange {
                                if (it) {
                                    progress = 0.0
                                    show()
                                } else {
                                    hide()
                                }
                            }
                        }
                        label {
                            icon(Ionicons4Material.ARROW_DROPRIGHT, 16, Color.WHITE)
                            size(16)
                            isVisible = false

                            song.isPlayingProperty.onChange {
                                isVisible = it && !song.isLoading
                            }
                            song.isLoadingProperty.onChange {
                                if (it) {
                                    hide()
                                } else {
                                    show()
                                }
                            }
                        }
                        vbox {
                            alignment = Pos.CENTER_LEFT
                            label(song.title) {
                                style {
                                    fontWeight = FontWeight.BOLD
                                    fontSize = 20.px
                                }
                                textOverrun = OverrunStyle.CLIP
                                margin(2.px)
                            }
                            label(song.authors.joinToString(" & ") { it.name }) {
                                style {
                                    fontWeight = FontWeight.LIGHT
                                    fontSize = 12.px
                                }
                                textOverrun = OverrunStyle.WORD_ELLIPSIS
                                margin(2.px)
                            }

                            val parent = parent as? Region ?: return@vbox
                            prefWidthProperty().bind(parent.widthProperty().subtract(64 * 3).subtract(4).subtract(16))
                            parent.widthProperty().onChange {
                                Platform.runLater {
                                    parent.requestLayout()
                                }
                            }
                        }

                        hbox(spacing = 16.0) {
                            hboxConstraints {
                                hGrow = Priority.ALWAYS
                                alignment = Pos.CENTER_RIGHT
                            }

                            button {
                                addClass(Style.bordered)

                                icon(Feather.FTH_CROSS, 32)
                                size(48)
                            }

                            button {
                                addClass(Style.bordered)

                                val play = icon(Ionicons4IOS.PLAY, 32)
                                size(48)

                                song.isPlayingProperty.onChange {
                                    if (it) {
                                        play.iconCode = Ionicons4IOS.PAUSE
                                    } else {
                                        play.iconCode = Ionicons4IOS.PLAY
                                    }
                                }

                                action {
                                    fire(SongChangeEvent(song))
                                }
                            }

                            button {
                                addClass(Style.bordered)

                                icon(Ionicons4IOS.STAR_OUTLINE, 32)
                                size(48)
                            }
                        }
                    }
                }
            }

            items = playlist.songsObservable
        }
        hbox(spacing = 8.0) {
            prefHeight = 64.0
            button {
                margin(8.px)

                addClass(Style.bordered)

                icon(Feather.FTH_PLUS)
                size(64)

                action {
                    customDialogView<AddSongDialog>(currentStage, "Add song", width = 400, height = 200) {
                        this.playlist = this@PlayList.playlist
                    }
                }
            }
        }

        playlist.songsObservable.onChange {
            playlist.serializeJson().saveInto(playlistPath)
        }
    }
}
