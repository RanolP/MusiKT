package io.github.ranolp.musikt.view

import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.*


class Style : Stylesheet() {
    companion object {
        val sliderProgressBar by cssclass()
        val realRoot by cssclass()
        val bordered by cssclass()
        val xray by cssclass()

        val musiktTheme = Color.hsb(240.0, 0.45, 0.65)
        val musiktThemeLight = Color.hsb(240.0, 0.25, 0.95)
        val darkForest = Color.hsb(0.0, 0.05, 0.15)
        val papertic = Color.hsb(230.0, 0.05, 0.95)
        val greeny = Color.hsb(130.0, 0.45, 0.85)
        val seliblue = Color.hsb(220.0, 0.45, 0.85)

        val iconColor by cssproperty<Paint>("-fx-icon-color")

        val base by cssclass("base")
    }


    init {
        FX.stylesheets.add("https://fonts.googleapis.com/css?family=Gothic+A1:100,200,300,400,500,600,700,800,900&subset=korean")
        star {
            textFill = papertic

           fontFamily = "Gothic A1"
        }

        form {
            backgroundColor += darkForest
        }

        realRoot {
            backgroundColor += darkForest
        }

        textField {
            backgroundColor += darkForest.brighter()
            borderColor += box(darkForest.darker())
            borderWidth += box(1.px)
            borderRadius += box(8.px)
        }

        button {
            backgroundColor += Color.TRANSPARENT

            and(hover) {
                backgroundColor += Color.gray(0.0, 0.2)
            }

            and(pressed) {
                backgroundColor += Color.gray(1.0, 0.08)
            }
        }

        bordered {
            borderColor += box(papertic.darker())
            borderStyle += BorderStrokeStyle.SOLID
            borderWidth += box(1.px)
        }

        xray {
            borderColor += box(c("#2d2"))
            borderStyle += BorderStrokeStyle.SOLID
            borderWidth += box(1.px)
            borderRadius += box(0.px)
        }

        slider {
            child(".thumb") {
                backgroundColor = multi(musiktThemeLight)
            }
        }

        sliderProgressBar {
            child(".bar") {
                padding = box(2.px)
                backgroundInsets += box(0.px)
            }
        }

        listView {
            backgroundColor += darkForest
        }

        listCell {
            and(odd) {
                backgroundColor += darkForest
            }
            and(even) {
                backgroundColor += darkForest.brighter()
            }
            and(selected) {
                backgroundColor += seliblue
            }
        }
    }
}
