package io.github.ranolp.musikt.model

import com.google.gson.JsonObject
import io.github.ranolp.musikt.source.Author
import io.github.ranolp.musikt.source.Source
import io.github.ranolp.musikt.source.SourceData
import io.github.ranolp.musikt.source.SourceGenerator
import io.github.ranolp.musikt.util.Progress
import io.github.ranolp.musikt.util.javafx.readOnly
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

data class Song<T : SourceData>(val data: JsonObject, val generator: SourceGenerator<T>) {
    val source: Source<T> by lazy {
        generator.generate(data)
    }

    val isPlayingProperty = SimpleBooleanProperty()

    var isPlaying by isPlayingProperty

    val titleProperty by lazy {
        readOnly("title") { title }
    }

    val title: String by lazy {
        source.data.title
    }

    val authorsProperty by lazy {
        readOnly<Set<Author>>("authors") { authors }
    }

    val authors: Set<Author> by lazy {
        source.data.authors
    }

    fun validate(progress: Progress? = null): Boolean {
        return try {
            source.get(progress)
            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

    fun validate(progress: (Long, Long, Double) -> Unit): Boolean {
        return validate(Progress.fromLambda(progress))
    }
}
