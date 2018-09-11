package io.github.ranolp.musikt.util.javafx

import javafx.beans.property.ReadOnlyProperty
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.stage.Stage
import tornadofx.*


val Stage.inResizing: Boolean
    get() = ResizeListener.listeners[this]?.inResize ?: false

class ResizeListener(private val stage: Stage,
        private val redundantPixels: ReadOnlyProperty<Insets> = readOnly<Insets>("insets") { Insets(0.0) }
) {
    companion object {
        internal val listeners = mutableMapOf<Stage, ResizeListener>()
    }

    init {
        listeners[stage] = this
    }

    private enum class Horizontal {
        LEFT,
        UNKNOWN,
        RIGHT,
        OUT
    }

    private enum class Vertical {
        TOP,
        UNKNOWN,
        BOTTOM,
        OUT
    }

    private lateinit var pressedLocation: Point2D
    private lateinit var pressedSize: Point2D
    private var horizontal = Horizontal.UNKNOWN
    private var vertical = Vertical.UNKNOWN
    var inResize = false

    init {
        val scene = stage.scene
        scene.setOnMouseMoved {
            val redundantPixels = redundantPixels.value
            val left = redundantPixels.left.toInt()
            val right = redundantPixels.right.toInt()
            val top = redundantPixels.top.toInt()
            val bottom = redundantPixels.bottom.toInt()
            fun range(midpoint: Int): IntRange = (midpoint - 5)..(midpoint + 5)

            horizontal = when {
                it.sceneX in range(left) -> Horizontal.LEFT
                it.sceneX in range(scene.width.toInt() + left - right) -> Horizontal.RIGHT
                it.sceneY in top..(scene.height.toInt() + bottom - top) -> Horizontal.UNKNOWN
                else -> Horizontal.OUT
            }
            vertical = when {
                it.sceneY in range(top) -> Vertical.TOP
                it.sceneY in range(scene.height.toInt() + top - bottom) -> Vertical.BOTTOM
                it.sceneX in left..(scene.width.toInt() + right - left) -> Vertical.UNKNOWN
                else -> Vertical.OUT
            }
            when (Pair(horizontal, vertical)) {
                Pair(
                    Horizontal.LEFT, Vertical.UNKNOWN
                ) -> {
                    scene.cursor = Cursor.W_RESIZE
                }
                Pair(
                    Horizontal.RIGHT, Vertical.UNKNOWN
                ) -> {
                    scene.cursor = Cursor.E_RESIZE
                }
                Pair(
                    Horizontal.UNKNOWN, Vertical.TOP
                ) -> {
                    scene.cursor = Cursor.N_RESIZE
                }
                Pair(
                    Horizontal.UNKNOWN, Vertical.BOTTOM
                ) -> {
                    scene.cursor = Cursor.S_RESIZE
                }
                Pair(
                    Horizontal.LEFT, Vertical.TOP
                ) -> {
                    scene.cursor = Cursor.NW_RESIZE
                }
                Pair(
                    Horizontal.RIGHT, Vertical.TOP
                ) -> {
                    scene.cursor = Cursor.NE_RESIZE
                }
                Pair(
                    Horizontal.LEFT, Vertical.BOTTOM
                ) -> {
                    scene.cursor = Cursor.SW_RESIZE
                }
                Pair(
                    Horizontal.RIGHT, Vertical.BOTTOM
                ) -> {
                    scene.cursor = Cursor.SE_RESIZE
                }
                else -> {
                    scene.cursor = Cursor.DEFAULT
                }
            }
        }

        scene.setOnMouseExited {
            if (!it.isPrimaryButtonDown) {
                return@setOnMouseExited
            }
            val redundantPixels = redundantPixels.value
            handle(it.screenX - redundantPixels.left, it.screenY - redundantPixels.top)
        }

        scene.setOnMouseDragged {
            val redundantPixels = redundantPixels.value
            handle(it.screenX - redundantPixels.left, it.screenY - redundantPixels.top)
        }

        scene.setOnMousePressed {
            val redundantPixels = redundantPixels.value
            if (horizontal != Horizontal.UNKNOWN || vertical != Vertical.UNKNOWN) {
                inResize = true
            }
            pressedLocation = point(it.screenX - redundantPixels.right, it.screenY - redundantPixels.bottom)
            pressedSize = point(scene.width, scene.height)
        }

        scene.setOnMouseReleased {
            inResize = false
        }
    }

    private fun handle(newX: Double, newY: Double) {
        val size = stage.screen.bounds

        val redundantPixels = redundantPixels.value

        val minWidth = stage.minWidth + redundantPixels.left + redundantPixels.right
        val minHeight = stage.minHeight + redundantPixels.top + redundantPixels.bottom

        when (horizontal) {
            Horizontal.LEFT -> {
                val from = stage.width
                val modified = pressedLocation.x - newX
                stage.width = minOf(size.width, maxOf(minWidth, pressedSize.x + modified))
                if (stage.width != from) {
                    stage.x = newX
                }
            }
            Horizontal.RIGHT -> {
                val modified = newX - pressedLocation.x
                stage.width = minOf(size.width, maxOf(minWidth, pressedSize.x + modified))
            }
            else -> {
                // do nothing
            }
        }
        when (vertical) {
            Vertical.TOP -> {
                val from = stage.height
                val modified = pressedLocation.y - newY
                stage.height = minOf(size.height, maxOf(minHeight, pressedSize.y + modified))
                if (stage.height != from) {
                    stage.y = newY
                }
            }
            Vertical.BOTTOM -> {
                val modified = newY - pressedLocation.y
                stage.height = minOf(size.height, maxOf(minHeight, pressedSize.y + modified))
            }
            else -> {
                // do nothing
            }
        }
    }
}
