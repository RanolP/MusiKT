package io.github.ranolp.musikt.view

import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.source.SourceGenerator
import io.github.ranolp.musikt.source.soundcloud.SoundCloudSourceGenerator
import io.github.ranolp.musikt.source.youtube.YoutubeSourceGenerator
import io.github.ranolp.musikt.util.SystemResourceLookup
import io.github.ranolp.musikt.util.javafx.customDialog
import io.github.ranolp.musikt.util.javafx.icon
import io.github.ranolp.musikt.util.javafx.margin
import io.github.ranolp.musikt.util.javafx.size
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import org.kordamp.ikonli.feather.Feather
import org.kordamp.ikonli.ionicons4.Ionicons4IOS
import tornadofx.*

class PlayList : View() {
    override val root = vbox {
        addClass(Style.realRoot)
        listview<Song<*>> {
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
                                margin(2.px)
                            }
                            label(song.authors.joinToString(" & ") { it.name }) {
                                style {
                                    fontWeight = FontWeight.LIGHT
                                    fontStyle = FontPosture.ITALIC
                                    fontSize = 12.px
                                }
                                margin(2.px)
                            }
                        }

                        hbox(spacing = 12.0) {
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

                                icon(Ionicons4IOS.PLAY, 32)
                                size(48)
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

            items = listOf(
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
                    customDialog {
                        combobox<SourceGenerator<*>>(
                            values = listOf(
                                YoutubeSourceGenerator,
                                SoundCloudSourceGenerator
                            )
                        ) {
                            cellFormat {
                                text = it.serviceName
                            }
                        }
                    }
                }
            }
        }
    }
}