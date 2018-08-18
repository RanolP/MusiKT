package io.github.ranolp.musikt.util.javafx

import io.github.ranolp.musikt.view.WindowTitle
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Ellipse
import javafx.stage.Modality
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.Window
import tornadofx.*

val Stage.screens: ObservableList<Screen>
    get() = Screen.getScreensForRectangle(x, y, width, height)
val Stage.screen: Screen
    get() = screens.first()


fun Node.coloring(color: Color, set: (Color) -> Unit) {
    set(color)

    hoverProperty().onChange {
        if (it) {
            set(color.darker())
        } else {
            set(color)
        }
    }
    pressedProperty().onChange {
        if (it) {
            set(color.brighter())
        } else {
            set(color)
        }
    }
}

fun Region.bindSize(parent: Region): Region {
    parent.prefWidthProperty().let {
        minWidthProperty().bind(it)
        maxWidthProperty().bind(it)
    }
    parent.prefHeightProperty().let {
        minHeightProperty().bind(it)
        maxHeightProperty().bind(it)
    }

    return this
}

fun customDialog(owner: Window? = null,
        title: String? = null,
        width: Number = 200,
        height: Number = 100,
        init: Form.(Stage) -> Unit
): CustomStage {
    val stage = CustomStage()
    stage.scene.stylesheets.addAll(FX.stylesheets)

    stage.apply(Form().also {
        it.spacing = 16.0
    }.also{
        it.init(stage)
    })
    title?.let {
        stage.title = it
    }
    stage.set(top = WindowTitle(stage, minimizeButton = { hide() }, maximizeButton = { hide() }).root)
    owner?.let {
        stage.initOwner(owner)
        stage.initModality(Modality.APPLICATION_MODAL)
    }

    stage.show()
    stage.minWidth = width.toDouble()
    stage.minHeight = height.toDouble()

    return stage
}

fun Node.fixVisible() {
    managedProperty().bind(visibleProperty())
}

fun Button.circular(radius: Number) {
    shape = circle(radius = radius)
    size(radius)
    shape.fill = Color.TRANSPARENT
}

fun Button.shaped(width: Number = this.width, height: Number = this.height): Button {
    this.shape = Ellipse(width.toDouble(), height.toDouble())
    return this
}

fun Button.size(radius: Number) {
    size(radius, radius)
}

fun Button.size(width: Number, height: Number) {
    minWidth = width.toDouble()
    minHeight = height.toDouble()
    prefWidth = width.toDouble()
    prefHeight = height.toDouble()
    maxWidth = width.toDouble()
    maxHeight = height.toDouble()
}
