package io.github.ranolp.musikt.util.javafx

import io.github.ranolp.musikt.view.WindowTitle
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
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
        init: (CustomStage) -> Unit
): CustomStage {
    val stage = CustomStage()
    stage.scene.stylesheets.addAll(FX.stylesheets)

    stage.apply(init)
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

inline fun customDialogForm(owner: Window? = null,
        title: String? = null,
        width: Number = 200,
        height: Number = 100,
        crossinline init: Form.(CustomStage) -> Unit
): CustomStage = customDialog(owner, title, width, height) { stage ->
    Form().also {
        it.spacing = 16.0
    }.also {
        it.init(stage)
    }
}

fun Node.fixVisible() {
    managedProperty().bind(visibleProperty())
}

fun Region.circular(radius: Number) {
    shape = circle(radius = radius)
    size(radius)
    shape.fill = Color.TRANSPARENT
}

fun Button.shaped(width: Number, height: Number): Button {
    size(width, height)
    shape = Rectangle(width.toDouble(), height.toDouble(), Color.WHITE)
    return this
}

fun Region.size(radius: Number) {
    size(radius, radius)
}

fun Region.size(width: Number, height: Number) {
    minWidth = width.toDouble()
    minHeight = height.toDouble()
    prefWidth = width.toDouble()
    prefHeight = height.toDouble()
    maxWidth = width.toDouble()
    maxHeight = height.toDouble()
}

fun Region.size(width: ObservableValue<out Number>, height: ObservableValue<out Number>) {
    minWidthProperty().bind(width)
    minHeightProperty().bind(height)
    prefWidthProperty().bind(width)
    prefHeightProperty().bind(height)
    maxWidthProperty().bind(width)
    maxHeightProperty().bind(height)
}
