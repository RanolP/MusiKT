package io.github.ranolp.musikt.util.javafx

import io.github.ranolp.musikt.view.Style
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.paint.Color
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

fun EventTarget.icon(iconCode: Ikon, iconSize: Int = 16, iconColor: Color = Color.BLACK, op: FontIcon.() -> Unit = {}
): FontIcon = FontIcon.of(iconCode, iconSize, iconColor).attachTo(this, op)

fun Button.icon(iconCode: Ikon, iconSize: Int = 16, iconColor: Color = Color.BLACK, op: FontIcon.() -> Unit = {}
): FontIcon = FontIcon.of(iconCode, iconSize, iconColor).also {
    isPickOnBounds = true
    this.graphic = it
    alignment = Pos.CENTER
    it.iconColor = Style.papertic
    contentDisplay = ContentDisplay.GRAPHIC_ONLY
    op(it)
}

fun Node.xray() {
    this.addClass(Style.xray)
}

fun Button.circular(radius: Number) {
    shape = circle(radius = radius)
    size(radius)
    shape.fill = Color.TRANSPARENT
}

fun Button.size(radius: Number) {
    minWidth = radius.toDouble()
    minHeight = radius.toDouble()
    prefWidth = radius.toDouble()
    prefHeight = radius.toDouble()
    maxWidth = radius.toDouble()
    maxHeight = radius.toDouble()
}

fun Node.margin(unit: Dimension<Dimension.LinearUnits>) {
    style(append = true) {
        padding = box(unit, unit)
        borderInsets += box(unit, unit)
        backgroundInsets += box(unit, unit)
    }
}
