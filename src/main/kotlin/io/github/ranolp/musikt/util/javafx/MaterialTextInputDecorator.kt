package io.github.ranolp.musikt.util.javafx

import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.*
import tornadofx.ValidationSeverity.*

class MaterialTextInputDecorator(private val message: String?, private val severity: ValidationSeverity) : Decorator {
    private val hue = when (severity) {
        Error -> 0.0
        Warning -> 35.0
        Info -> 230.0
        Success -> 120.0
    }
    private val lineColor = Color.hsb(hue, 0.65, 0.85)
    private val textColor = Color.hsb(hue, 0.45, 0.95)
    lateinit var tag: Line
    lateinit var label: Label
    lateinit var attachedToNode: Node

    override fun decorate(node: Node) {
        attachedToNode = node

        if (message?.isNotBlank() == true) {
            label = Label(message)
            label.style = "-fx-text-fill: ${textColor.css};"
            node.add(label)
        }

        if (node is Region) {
            label.translateXProperty()
                    .bind(node.widthProperty().divide(-2).add(label.layoutBoundsProperty().doubleBinding {
                        (it?.width ?: 0.0) / 2.0
                    }).add(2))
            label.translateYProperty().bind(node.heightProperty())

            tag = node.line(0.0, node.height, node.width, node.height + 2.0) {
                endXProperty().bind(node.widthProperty())
                isManaged = false
                stroke = lineColor
            }
        }
    }

    override fun undecorate(node: Node) {
        tag.removeFromParent()
        label.removeFromParent()
    }
}

