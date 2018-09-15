package io.github.ranolp.musikt.view

import io.github.ranolp.musikt.model.Playlist
import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.source.SourceGenerator
import io.github.ranolp.musikt.source.guessYoutubeId
import io.github.ranolp.musikt.source.isValidSoundCloudUrl
import io.github.ranolp.musikt.source.makeSoundcloudInput
import io.github.ranolp.musikt.source.makeYoutubeInput
import io.github.ranolp.musikt.source.soundcloud.SoundCloudSourceGenerator
import io.github.ranolp.musikt.source.youtube.YoutubeSourceGenerator
import io.github.ranolp.musikt.util.javafx.Dialog
import io.github.ranolp.musikt.util.javafx.MaterialTextInputDecorator
import io.github.ranolp.musikt.util.javafx.circularProgressBar
import io.github.ranolp.musikt.util.javafx.coloring
import io.github.ranolp.musikt.util.javafx.shaped
import io.github.ranolp.musikt.util.javafx.size
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.paint.Color
import tornadofx.*

class AddSongDialog : Dialog() {
    lateinit var playlist: Playlist

    override val root = form {
        spacing = 4.0

        addClass(Style.realRoot)

        val combobox = combobox(values = SourceGenerator.generators.toList()) {
            cellFormat {
                text = it.serviceName
            }
        }

        val context = ValidationContext()
        context.decorationProvider = {
            MaterialTextInputDecorator(it.message, it.severity)
        }

        val youtubeData: StringProperty = SimpleStringProperty()

        val youtube = fieldset("Youtube Settings") {
            field("Url or Video Id") {
                val input = textfield()
                youtubeData.bind(input.textProperty())
                context.addValidator(input, input.textProperty()) {
                    if (it == null || it.isBlank()) {
                        ValidationMessage("Required", ValidationSeverity.Error)
                    } else if (it.guessYoutubeId == null) {
                        ValidationMessage("It must be valid url or id", ValidationSeverity.Error)
                    } else {
                        ValidationMessage("All is OK!", ValidationSeverity.Info)
                    }
                }
            }
        }


        val soundcloudUrl: StringProperty = SimpleStringProperty()

        val soundcloud = fieldset("SoundCloud Settings") {
            field("SoundCloud Song Url") {
                val input = textfield()
                soundcloudUrl.bind(input.textProperty())
                context.addValidator(input, input.textProperty()) {
                    if (it == null || it.isBlank()) {
                        ValidationMessage("Required", ValidationSeverity.Error)
                    } else if (!it.isValidSoundCloudUrl) {
                        ValidationMessage("It must be valid url", ValidationSeverity.Error)
                    } else {
                        ValidationMessage("All is OK!", ValidationSeverity.Info)
                    }
                }
            }
        }

        hbox(spacing = 8.0) {
            val progressBar = circularProgressBar {
                size(28.0)
                progress = 0.0
                strokeColor = Style.musiktThemeLight
                strokeWidth = 2.0
            }

            button("Add") {
                addClass(Style.bordered)
                val addButton = this
                shaped(150, 30)

                style {
                    fontSize = 16.px
                }

                coloring(Color.hsb(0.0, 1.0, 0.85), shape::setFill)

                action {
                    val generator = combobox.value
                    val song = when (generator) {
                        YoutubeSourceGenerator -> {
                            Song(
                                makeYoutubeInput(
                                    youtubeData.get().guessYoutubeId!!
                                ), generator
                            )
                        }
                        SoundCloudSourceGenerator -> {
                            Song(
                                makeSoundcloudInput(
                                    soundcloudUrl.get()
                                ), generator
                            )
                        }
                        else -> null
                    }
                    if (song != null) {
                        addButton.isDisable = true

                        runAsync {
                            song.validate { percent ->
                                Platform.runLater {
                                    progressBar.progress = percent / 100
                                }
                            }
                        }.success { isValid ->
                            if (isValid) {
                                playlist.addSong(song)
                                stage.close()
                            } else {
                                addButton.isDisable = false
                            }
                        }.fail {
                            addButton.isDisable = false
                        }
                    }
                }
            }
        }

        combobox.valueProperty().onChange {
            youtube.hide()
            soundcloud.hide()
            when (it) {
                YoutubeSourceGenerator -> {
                    youtube.show()
                }
                SoundCloudSourceGenerator -> {
                    soundcloud.show()
                }
            }
        }

        combobox.selectionModel.select(0)
    }
}
