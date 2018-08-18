package io.github.ranolp.musikt.source

import io.github.ranolp.musikt.util.Progress
import io.github.ranolp.musikt.util.convertTo
import io.github.ranolp.musikt.util.parseJson
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.BiPredicate
import kotlin.streams.toList

interface Source<Data : SourceData> {
    companion object {
        inline fun search(path: Path, crossinline namePredicate: (String) -> Boolean): List<Path> {
            return Files.find(path, 1, BiPredicate { p, _ ->
                namePredicate(p.fileName.toString())
            }).toList()
        }

        inline fun <reified T> read(path: Path): T {
            return Files.newBufferedReader(path).parseJson().convertTo()
        }
    }

    enum class Type(val canListen: Boolean, val canWatch: Boolean) {
        AUDIO(true, false),
        VIDEO(true, true),
        AUDIOLESS_VIDEO(false, true),
        UNKNOWN(false, false)
    }

    data class Result(val isCached: Boolean, val type: Type, val path: Path)

    val data: Data

    fun get(progress: Progress? = null): List<Result>

    fun get(progress: (Long, Long, Double) -> Unit): List<Result> {
        return get(Progress.fromLambda(progress))
    }
}
