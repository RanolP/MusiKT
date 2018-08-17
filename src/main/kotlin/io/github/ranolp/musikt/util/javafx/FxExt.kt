package io.github.ranolp.musikt.util.javafx

import io.github.ranolp.musikt.view.WindowTitle
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage
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

fun customDialog(init: StageAwareFieldset.() -> Unit): CustomStage {
    val stage = CustomStage()
    stage.apply(StageAwareFieldset().also(init))
    stage.show()
    stage.set(top = WindowTitle(stage, minimizeButton = { hide() }, maximizeButton = { hide() }).root)
    stage.minWidth = 200.0
    stage.minHeight = 100.0
    return stage
}
