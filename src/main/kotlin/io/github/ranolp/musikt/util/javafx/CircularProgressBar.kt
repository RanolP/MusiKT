package io.github.ranolp.musikt.util.javafx

import javafx.animation.Interpolator
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Arc
import javafx.util.Duration
import tornadofx.*
import kotlin.concurrent.timer


class CircularProgressBar : Region() {
    private val fillerArc = Arc().also {
        it.startAngle = 90.0
        it.fill = Color.TRANSPARENT
    }
    private val transition = sequentialTransition {
        timeline {
            this.keyframe(Duration.millis(500.0)) {
                keyvalue(fillerArc.startAngleProperty(), 90.0 - 360.0, Interpolator.EASE_BOTH)
            }
        }
    }

    val progressProperty = SimpleDoubleProperty()
        @JvmName("progressProperty") get

    var progress by progressProperty

    val strokeWidthProperty = SimpleDoubleProperty()
        @JvmName("strokeWidthProperty") get

    var strokeWidth by strokeWidthProperty

    val strokeColorProperty = SimpleObjectProperty<Color>()
    var strokeColor by strokeColorProperty


    init {
        children.addAll(fillerArc)

        fillerArc.let {
            it.centerXProperty().bind(widthProperty().divide(2))
            it.radiusXProperty().bind(widthProperty().divide(2))

            it.centerYProperty().bind(heightProperty().divide(2))
            it.radiusYProperty().bind(heightProperty().divide(2))
        }


        progressProperty.onChange {
            fillerArc.length = when {
                it >= 1 -> -360.0
                it >= 0 -> minOf(it, 1.0) * -360
                else -> 2.0
            }
        }

        timer(daemon = true, period = 3000) {
            if (!isVisible) {
                return@timer
            }

            Platform.runLater {
                transition.play()
            }

        }

        fillerArc.strokeWidthProperty().bind(strokeWidthProperty)
        fillerArc.strokeProperty().bind(strokeColorProperty)
    }
}
