package io.github.ranolp.musikt.util.javafx

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.ReadOnlyProperty
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.effect.DropShadow
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

class CustomStage(val shadow: Int = 16, val fitScene: Boolean = true) : Stage(StageStyle.TRANSPARENT) {
    private val shadowPane = BorderPane()
    val real: Node
        get() = shadowPane.center
    private val realScene = Scene(shadowPane)

    private val shadow_ = shadow.toDouble()
    lateinit var insets: Insets
        private set

    val insetsProperty: ReadOnlyProperty<Insets> by lazy {
        readOnly<Insets>("insets") { insets }
    }

    init {
        updateShadow()
        minWidthProperty().onChange {
            width = maxOf(width, it + insets.left + insets.right)
        }
        minHeightProperty().onChange {
            height = maxOf(height, it + insets.top + insets.bottom)
        }
        shadowPane.style = "-fx-background-color: transparent;"
        shadowPane.padding = insets
        realScene.fill = Color.TRANSPARENT

        scene = realScene

        apply(AnchorPane())

        focusedProperty().onChange {
            updateShadow()
        }

        ResizeListener(this, insetsProperty)
    }

    private fun updateShadow() {
        if (isFocused) {
            shadowPane.effect = DropShadow(
                shadow_ * 1.5, shadow_ * 0.25, shadow_ * 0.25, Color.gray(0.0, 0.65)
            )
            insets = Insets(
                shadow_, shadow_ * 1.75, shadow_ * 1.75, shadow_
            )
        } else {
            shadowPane.effect = DropShadow(
                shadow_, shadow_ * 0.15, shadow_ * 0.15, Color.gray(0.0, 0.65)
            )
            insets = Insets(
                shadow_ * 1.15, shadow_, shadow_, shadow_ * 1.15
            )
        }
    }

    private val centerWidth: DoubleBinding
        get() {

            var result = widthProperty().subtract(insets.left + insets.right)

            shadowPane.left?.let {
                if (it is Region) {
                    result = result.subtract(it.widthProperty())
                }
            }
            shadowPane.right?.let {
                if (it is Region) {
                    result = result.subtract(it.widthProperty())
                }
            }

            return result
        }

    private val centerHeight: DoubleBinding
        get() {

            var result = heightProperty().subtract(insets.top + insets.bottom)

            shadowPane.top?.let {
                if (it is Region) {
                    result = result.subtract(it.heightProperty())
                }
            }
            shadowPane.bottom?.let {
                if (it is Region) {
                    result = result.subtract(it.heightProperty())
                }
            }

            return result
        }

    fun apply(root: Node): Scene {
        shadowPane.center = root
        if (fitScene && root is Region) {
            centerWidth.let {
                root.minWidthProperty().bind(it)
                root.maxWidthProperty().bind(it)
            }
            centerHeight.let {
                root.minHeightProperty().bind(it)
                root.maxHeightProperty().bind(it)
            }
        }
        return realScene
    }

    fun apply(scene: Scene): Scene = apply(scene.root)

    fun delegateStage(stage: Stage) {
        stage.sceneProperty().onChange {
            it?.run(::apply)
        }
    }

    fun set(top: Node? = null, right: Node? = null, bottom: Node? = null, left: Node? = null) {
        top?.let {
            shadowPane.top = it
        }
        right?.let {
            shadowPane.top = it
        }
        bottom?.let {
            shadowPane.top = it
        }
        left?.let {
            shadowPane.top = it
        }
    }
}
