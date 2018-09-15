package io.github.ranolp.musikt.view

import io.github.ranolp.musikt.util.javafx.CustomStage
import io.github.ranolp.musikt.util.javafx.DragListener
import io.github.ranolp.musikt.util.javafx.circular
import io.github.ranolp.musikt.util.javafx.coloring
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import kotlin.system.exitProcess

class WindowTitle(private val stage: CustomStage,
        closeButton: (ButtonConfiguration.() -> Unit)? = null,
        minimizeButton: (ButtonConfiguration.() -> Unit)? = null,
        maximizeButton: (ButtonConfiguration.() -> Unit)? = null
) : Fragment() {
    private val closeConfiguration = ButtonConfiguration {
        while (stage.opacity > 0.1) {
            stage.opacity -= 0.1
            Thread.sleep(10)
        }
        stage.close()
        exitProcess(0)
    }.also {
        closeButton?.invoke(it)
    }
    private val minimizeConfiguration = ButtonConfiguration {
        stage.isIconified = true
    }.also {
        minimizeButton?.invoke(it)
    }
    private val maximizeConfiguration = ButtonConfiguration {
        stage.isMaximized = !stage.isMaximized
    }.also {
        maximizeButton?.invoke(it)
    }

    class ButtonConfiguration(internal var show: Boolean = true, internal var action: () -> Unit) {
        fun action(action: () -> Unit) {
            this.action = action
        }

        fun hide() {
            show = false
        }
    }

    private fun ButtonConfiguration?.execute(orElse: () -> Unit) {
        (this?.action ?: orElse)()
    }

    companion object {
        const val height = 24.0
    }

    override val root: BorderPane = borderpane {
        vboxConstraints {
            vGrow = Priority.NEVER
        }
        prefHeight = WindowTitle.height
        minHeight = WindowTitle.height
        maxHeight = WindowTitle.height
        minWidthProperty().bind(stage.widthProperty().subtract(stage.insets.left + stage.insets.right))
        DragListener(this, stage)

        style {
            backgroundColor += Color.BLACK
        }

        var buttonCount = 0

        left = hbox(spacing = 12.0, alignment = Pos.CENTER_LEFT) {
            paddingLeft = 8.0
            if (closeConfiguration.show) {
                button {
                    circular(6)

                    coloring(Color.hsb(0.0, 1.0, 0.85), shape::setFill)

                    action {
                        closeConfiguration.action()
                    }
                }
                buttonCount++
            }
            if (minimizeConfiguration.show) {
                button {
                    circular(6)

                    coloring(Color.hsb(50.0, 1.0, 0.85), shape::setFill)

                    action {
                        minimizeConfiguration.action()
                    }
                }
                buttonCount++
            }
            if (maximizeConfiguration.show) {
                button {
                    circular(6)

                    coloring(Color.hsb(120.0, 1.0, 0.85), shape::setFill)

                    action {
                        maximizeConfiguration.action()
                    }
                }
                buttonCount++
            }
        }

        center = label {
            textProperty().bind(stage.titleProperty())
            paddingLeft = 0 - 8 - 10 * buttonCount
            style {
                fontWeight = FontWeight.LIGHT
                fontSize = 16.px
                textFill = Color.hsb(0.0, 0.0, 0.75)
            }
        }
    }
}
