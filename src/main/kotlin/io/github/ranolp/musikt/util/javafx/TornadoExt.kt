package io.github.ranolp.musikt.util.javafx

import io.github.ranolp.musikt.view.Style
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Labeled
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.Window
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

fun EventTarget.icon(iconCode: Ikon, iconSize: Int = 16, iconColor: Color = Color.BLACK, op: FontIcon.() -> Unit = {}
): FontIcon = FontIcon.of(iconCode, iconSize, iconColor).attachTo(this, op)

fun Labeled.icon(iconCode: Ikon, iconSize: Int = 16, iconColor: Color = Color.BLACK, op: FontIcon.() -> Unit = {}
): FontIcon = FontIcon.of(iconCode, iconSize, iconColor).also {
    isPickOnBounds = true
    this.graphic = it
    alignment = Pos.CENTER
    it.iconColor = Style.papertic
    contentDisplay = ContentDisplay.GRAPHIC_ONLY
    op(it)
}

fun EventTarget.circularProgressBar(op: CircularProgressBar.() -> Unit = {}) = CircularProgressBar().attachTo(this, op)

fun Node.xray() {
    this.addClass(Style.xray)
}

fun Node.margin(unit: Dimension<Dimension.LinearUnits>) {
    style(append = true) {
        padding = box(unit, unit)
        borderInsets += box(unit, unit)
        backgroundInsets += box(unit, unit)
    }
}

inline fun <reified T : Dialog> UIComponent.customDialogView(owner: Window? = null,
        title: String? = null,
        width: Number = 200,
        height: Number = 100,
        reset: Boolean = true,
        crossinline init: T.(Stage) -> Unit
): CustomStage = customDialog(owner, title, width, height) {
    val dialog = T::class.java.newInstance()
    (dialog as? UIComponent)?.init()
    dialog.stage = it
    it.apply(dialog.root)
    dialog.init(it)
}

abstract class Dialog(title: String? = null, icon: Node? = null) : View(title, icon) {
    lateinit var stage: CustomStage
}
