package io.github.ranolp.musikt.source.soundcloud

import com.google.gson.annotations.SerializedName
import io.github.ranolp.musikt.SOUNDCLOUD_KEY
import io.github.ranolp.musikt.SOURCE_CACHE_FOLDER
import io.github.ranolp.musikt.source.Author
import io.github.ranolp.musikt.source.Source
import io.github.ranolp.musikt.util.Progress
import io.github.ranolp.musikt.util.convertTo
import io.github.ranolp.musikt.util.deserialize
import io.github.ranolp.musikt.util.parseJson
import io.github.ranolp.musikt.util.serialize
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.file.Files

class SoundCloudSource(url: String) : Source<SoundCloudData> {
    private data class Track(val id: Long, @SerializedName("stream_url") val streamUrl: String,
            val title: String,
            val user: User, @SerializedName("original_format") val format: String
    )

    private data class User(val username: String, @SerializedName("permalink_url") val url: String)
    companion object {
        val path = SOURCE_CACHE_FOLDER.resolve("soundcloud")
        private const val BUFFER_SIZE = 8192

        init {
            if (!path.toFile().exists()) {
                Files.createDirectories(path)
            }
        }
    }

    private val track by lazy {
        val resolve = URL("http://api.soundcloud.com/resolve?client_id=$SOUNDCLOUD_KEY&url=$url")
        val resolvedUrl = resolve.readText().parseJson().asJsonObject["location"].asString
        URL(resolvedUrl).readText().parseJson().convertTo<Track>()
    }

    override val data: SoundCloudData by lazy {
        val file = Source.search(path) {
            it == "${track.id}.object"
        }.firstOrNull()
        if (file != null) {
            return@lazy file.deserialize<SoundCloudData>()
        }

        val result = SoundCloudData(
            false, track.title, setOf(Author(track.user.username, setOf(SoundCloudConnection(track.user.url))))
        )
        result.copy(isCached = true).serialize(path.resolve("${track.id}.object"))
        result
    }

    override fun get(progress: Progress?): List<Source.Result> {
        val files = Source.search(path) {
            it.startsWith(track.id.toString()) && !it.endsWith(".object")
        }
        if (files.size == 1) {
            return listOf(Source.Result(true, Source.Type.AUDIO, files[0]))
        } else if (files.isNotEmpty()) {
            println(files)
        }

        val connection = URL("${track.streamUrl}?client_id=$SOUNDCLOUD_KEY").openConnection()
        connection.connect()

        var current = 0L
        val max = connection.contentLength.toLong() * 2

        val bytes = connection.inputStream.use {
            val baos = ByteArrayOutputStream(Math.max(BUFFER_SIZE, it.available()))

            val buffer = ByteArray(BUFFER_SIZE)
            var bytes = it.read(buffer)
            while (bytes >= 0) {
                baos.write(buffer, 0, bytes)
                bytes = it.read(buffer)
                current += bytes
                progress?.notify(current, max, 100.0 * current / max)
            }

            baos.toByteArray()
        }
        val target = path.resolve("${track.id}.${track.format}")

        Files.newOutputStream(target).use { out ->
            val len = bytes.size
            var rem = len
            while (rem > 0) {
                val n = Math.min(rem, BUFFER_SIZE)
                out.write(bytes, len - rem, n)
                rem -= n
                current += n
                progress?.notify(current, max, 100.0 * current / max)
            }
        }

        progress?.complete(max)

        return listOf(Source.Result(false, Source.Type.AUDIO, target))
    }
}
