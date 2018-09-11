package io.github.ranolp.musikt.source

import io.github.ranolp.musikt.util.Progress
import io.github.ranolp.musikt.util.convertTo
import io.github.ranolp.musikt.util.parseJson
import ws.schild.jave.AudioAttributes
import ws.schild.jave.Encoder
import ws.schild.jave.EncodingAttributes
import ws.schild.jave.MultimediaObject
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.BiPredicate
import java.util.regex.Pattern
import kotlin.streams.toList


interface Source<Data : SourceData> {
    companion object {
        private val SUCCESS_PATTERN = Pattern.compile(
            "^\\s*video\\:\\S+\\s+audio\\:\\S+\\s+subtitle\\:\\S+\\s+global headers\\:\\S+.*$", Pattern.CASE_INSENSITIVE
        )

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
        WAV(true, false),
        UNKNOWN(false, false)
    }

    data class Result(val isCached: Boolean, val type: Type, val path: Path)

    val data: Data

    fun get(progress: Progress? = null): List<Result>

    fun get(progress: (Double) -> Unit): List<Result> {
        return get(Progress.fromLambda(progress))
    }

    fun makeWav(source: File): Result {
        val target = File(source.parent, source.nameWithoutExtension + ".wav")

        if (target.exists()) {
            return Result(true, Type.WAV, target.toPath())
        }

        val audio = AudioAttributes()
        audio.setBitRate(128 * 1000) // 128 kBps
        audio.setChannels(2)
        audio.setSamplingRate(44100) // 44100 hz

        val attrs = EncodingAttributes()
        attrs.setFormat("wav")
        attrs.setAudioAttributes(audio)

        val encoder = Encoder()
        encoder.encode(MultimediaObject(source), target, attrs)

        return Result(false, Type.WAV, target.toPath())
    }
}
