package io.github.ranolp.musikt.util

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import tornadofx.*
import java.io.InputStream
import java.net.URL


object SystemResourceLookup {
    operator fun get(resource: String): String = ClassLoader.getSystemResource(resource).toExternalForm()
    fun url(resource: String): URL = ClassLoader.getSystemResource(resource)
    fun stream(resource: String): InputStream = ClassLoader.getSystemResourceAsStream(resource)
    fun image(resource: String): Image = Image(stream(resource))
    fun imageview(resource: String, lazyload: Boolean = false): ImageView = ImageView(Image(url(resource).toExternalForm(), lazyload))

    fun json(resource: String) = stream(resource).toJSON()

    fun jsonArray(resource: String) = stream(resource).toJSONArray()

    fun gson(resource: String) = stream(resource).parseJson().asJsonObject

    fun gsonArray(resource: String) = stream(resource).parseJson().asJsonArray

    fun text(resource: String): String = stream(resource).use { it.bufferedReader().readText() }
}
