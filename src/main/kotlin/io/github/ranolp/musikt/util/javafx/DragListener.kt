package io.github.ranolp.musikt.util.javafx

import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.stage.Stage
import tornadofx.*

class DragListener(node: Node, private val stage: Stage) {
    lateinit var pressed: Point2D

    init {
        node.setOnMousePressed {
            pressed = point(it.sceneX, it.sceneY)
        }

        node.setOnMouseDragged {
            if (stage.inResizing) {
                return@setOnMouseDragged
            }
            val size = stage.screen.bounds
            stage.x = minOf(size.width - stage.width, maxOf(0.0, it.screenX - pressed.x))
            stage.y = minOf(size.height - stage.height, maxOf(0.0, it.screenY - pressed.y))
        }
    }
}
