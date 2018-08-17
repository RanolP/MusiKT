package io.github.ranolp.musikt.view

import io.github.ranolp.musikt.controller.MusicPlayerController
import io.github.ranolp.musikt.util.SystemResourceLookup
import io.github.ranolp.musikt.util.javafx.circular
import io.github.ranolp.musikt.util.javafx.icon
import io.github.ranolp.musikt.util.javafx.margin
import io.github.ranolp.musikt.util.javafx.size
import javafx.beans.property.StringProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import org.kordamp.ikonli.feather.Feather
import org.kordamp.ikonli.ionicons4.Ionicons4IOS
import tornadofx.*

class MusicPlayer : View() {
    val controller: MusicPlayerController by inject()

    override val root = vbox {
        val root = this
        addClass(Style.realRoot)

        imageview {
            vboxConstraints {
                alignment = Pos.CENTER
            }
            margin(8.px)

            isPreserveRatio = true

            val hMargin = 16 * 2
            val vMargin = 64 + 40 + 92 + 16 * 3

            root.heightProperty().onChange {
                isVisible = it > vMargin + 64
            }

            image = SystemResourceLookup.image("mockup/mockup.png")

            managedProperty().bind(visibleProperty())

            fitWidthProperty().bind(root.widthProperty().subtract(hMargin))
            fitHeightProperty().bind(root.heightProperty().subtract(vMargin))

            isVisible = false
        }

        alignment = Pos.BOTTOM_CENTER

        hbox {
            margin(8.px)


            minHeight = 64.0
            button {
                addClass(Style.bordered)
                margin(8.px)
                circular(48)

                icon(Feather.FTH_PLUS)
            }
            vbox {
                hboxConstraints {
                    hgrow = Priority.ALWAYS
                }
                alignment = Pos.CENTER

                label("Ylvis") {
                    style {
                        fontWeight = FontWeight.BLACK
                        fontSize = 20.px
                    }
                }

                label("The fox") {
                    style {
                        fontWeight = FontWeight.LIGHT
                        fontSize = 12.px
                    }
                }
            }
            button {
                addClass(Style.bordered)
                margin(8.px)
                circular(48)

                icon(Feather.FTH_ELLIPSIS)
            }
        }
        vbox {
            margin(8.px)

            lateinit var currentProperty: StringProperty
            lateinit var maxProperty: StringProperty

            hbox {
                minHeight = 20.0
                currentProperty = label().textProperty()

                anchorpane {
                    hgrow = Priority.ALWAYS
                }

                maxProperty = label().textProperty()
            }
            stackpane {
                val parent = this

                val progress = progressbar(0.0) {
                    addClass(Style.sliderProgressBar)
                    prefWidthProperty().bind(parent.widthProperty().subtract(16))


                    style = """
                        -fx-accent: ${Style.musiktThemeLight.brighter().css};
                        -fx-control-inner-background: ${Style.papertic.css};
                    """.trimIndent()

                }.progressProperty()

                slider {
                    prefWidthProperty().bind(parent.widthProperty().subtract(16 + 64))
                    minHeight = 20.0

                    progress.bind(valueProperty().divide(maxProperty()))

                    valueProperty().onChange {
                        val hour = it.toInt() / 60 / 60
                        val minute = (it.toInt() / 60 % 60).toString().padStart(2, '0')
                        val second = (it.toInt() % 60).toString().padStart(2, '0')
                        val label = if (hour > 0) {
                            "${hour.toString().padStart(2, '0')}:$minute:$second"
                        } else {
                            "$minute:$second"
                        }
                        currentProperty.set(label)
                    }

                    maxProperty().onChange {
                        val hour = it.toInt() / 60 / 60
                        val minute = (it.toInt() / 60 % 60).toString().padStart(2, '0')
                        val second = (it.toInt() % 60).toString().padStart(2, '0')
                        val label = if (hour > 0) {
                            "${hour.toString().padStart(2, '0')}:$minute:$second"
                        } else {
                            "$minute:$second"
                        }
                        maxProperty.set(label)
                    }

                    max = 3 * 60.0 + 25.0
                    value = 60 + 30.0

                    style = """
                        -fx-control-inner-background: transparent;
                        -fx-background: null;
                    """.trimIndent()
                }
            }
        }
        hbox {
            vboxConstraints {
                alignment = Pos.BOTTOM_CENTER
            }
            margin(8.px)

            minHeight = 92.0

            hboxConstraints {
                hgrow = Priority.ALWAYS
            }
            alignment = Pos.CENTER
            button {
                margin(8.px)
                size(64.0)

                val btn = icon(Feather.FTH_SHUFFLE)

                action {
                    if (controller.isShuffleModeEnabled) {
                        controller.disableShuffleMode()
                        btn.iconColor = Style.papertic
                    } else {
                        controller.enableShuffleMode()
                        btn.iconColor = Style.greeny
                    }
                }
            }
            button {
                margin(8.px)
                size(76)

                icon(Feather.FTH_SKIP_BACK, 24)

            }
            button {
                addClass(Style.bordered)

                margin(8.px)
                circular(76)

                val btn = icon(Ionicons4IOS.PLAY, 24)

                action {
                    if (controller.isPlaying) {
                        btn.iconCode = Ionicons4IOS.PLAY
                        controller.pauseSong()
                    } else {
                        btn.iconCode = Ionicons4IOS.PAUSE
                        controller.startSong()
                    }
                }
            }
            button {
                margin(8.px)
                size(76.0)

                icon(Feather.FTH_SKIP_FORWARD, 24)
            }
            button {
                margin(8.px)
                size(64.0)

                val btn = icon(Feather.FTH_REPEAT)

                action {
                    if (controller.isRepeatModeEnabled) {
                        controller.disableRepeatMode()
                        btn.iconColor = Style.papertic
                    } else {
                        controller.enableRepeatMode()
                        btn.iconColor = Style.greeny
                    }
                }
            }
        }
    }
}
