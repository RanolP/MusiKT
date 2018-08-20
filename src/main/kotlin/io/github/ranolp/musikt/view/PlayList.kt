package io.github.ranolp.musikt.view

import io.github.ranolp.musikt.event.SongChangeEvent
import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.source.soundcloud.SoundCloudSourceGenerator
import io.github.ranolp.musikt.source.youtube.YoutubeSourceGenerator
import io.github.ranolp.musikt.util.SystemResourceLookup
import io.github.ranolp.musikt.util.javafx.customDialogView
import io.github.ranolp.musikt.util.javafx.icon
import io.github.ranolp.musikt.util.javafx.margin
import io.github.ranolp.musikt.util.javafx.size
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.OverrunStyle
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.text.FontWeight
import org.kordamp.ikonli.feather.Feather
import org.kordamp.ikonli.ionicons4.Ionicons4IOS
import tornadofx.*

class PlayList : View() {
    override val root = vbox {
        addClass(Style.realRoot)
        val songs = listview<Song<*>> {
            vboxConstraints {
                vgrow = Priority.ALWAYS
            }
            fixedCellSize = 64.0
            cellFormat { song ->
                graphic = cache {
                    hbox(spacing = 8.0) {
                        alignment = Pos.CENTER_LEFT
                        minHeight = 64.0
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
                            prefWidthProperty().bind(parent.widthProperty().subtract(64 * 3).subtract(4))
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

            items = mutableListOf(
                Song(SystemResourceLookup.gson("mockup/youtube/0.json"), YoutubeSourceGenerator),
                Song(SystemResourceLookup.gson("mockup/youtube/1.json"), YoutubeSourceGenerator),
                Song(SystemResourceLookup.gson("mockup/soundcloud/0.json"), SoundCloudSourceGenerator)
            ).observable()
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
                        this.songs = songs
                    }
                }
            }
        }
    }
}
