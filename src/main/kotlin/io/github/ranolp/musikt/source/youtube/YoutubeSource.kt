package io.github.ranolp.musikt.source.youtube

import com.github.axet.vget.VGet
import com.github.axet.vget.info.VideoInfo
import com.github.axet.vget.vhs.YouTubeInfo
import com.github.axet.vget.vhs.YouTubeMPGParser
import com.github.axet.wget.info.URLInfo
import io.github.ranolp.musikt.SOURCE_CACHE_FOLDER
import io.github.ranolp.musikt.source.Author
import io.github.ranolp.musikt.source.Source
import io.github.ranolp.musikt.source.Source.Companion.search
import io.github.ranolp.musikt.util.Progress
import io.github.ranolp.musikt.util.deserialize
import io.github.ranolp.musikt.util.serialize
import org.jsoup.Jsoup
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicBoolean


class YoutubeSource(val id: String) : Source<YoutubeData> {
    companion object {
        val parser = YouTubeMPGParser()
        val path = SOURCE_CACHE_FOLDER.resolve("youtube")

        init {
            if (!path.toFile().exists()) {
                Files.createDirectories(path)
            }
        }
    }

    override val data: YoutubeData by lazy {
        val file = search(path) {
            it == "$id.object"
        }.firstOrNull()
        if (file != null) {
            return@lazy file.deserialize<YoutubeData>()
        }

        val url = "https://youtube.com/watch?v=$id&feature=oembed"
        val data = URL(url).openStream().readBytes().toString(Charsets.UTF_8)
        val document = Jsoup.parse(data)

        val result = YoutubeData(
            false, document.title().removeSuffix(" - YouTube"), setOf(
                Author(
                    document.select("img[data-ytimg=1]").attr("alt"), setOf(
                        YoutubeConnection(document.select("span[itemprop=author] link").attr("href"))
                    )
                )
            )
        )

        result.copy(isCached = true).serialize(path.resolve("$id.object"))
        result
    }

    private fun map(paths: List<File>, cached: Boolean): List<Source.Result> {
        return paths.map { file ->
            val ext = file.extension
            val renameTarget = path.resolve("$id.$ext").toFile()
            file.renameTo(renameTarget)
            Source.Result(
                cached, when (ext) {
                    "mp4" -> {
                        if (paths.any { it.extension == "webm" }) {
                            Source.Type.AUDIOLESS_VIDEO
                        } else {
                            Source.Type.VIDEO
                        }
                    }
                    "webm" -> Source.Type.AUDIO
                    else -> Source.Type.UNKNOWN
                }, renameTarget.toPath()
            )
        }
    }

    override fun get(progress: Progress?): List<Source.Result> {
        val files = search(path) {
            it.startsWith(id) && !it.endsWith(".object")
        }.map { it.toFile() }
        if (files.isNotEmpty()) {
            return map(files, cached = true)
        }

        val stop = AtomicBoolean(false)
        // TODO 화질 필터 필요
        val webUrl = URL("https://www.youtube.com/watch?v=$id")
        val videoInfo = parser.info(webUrl) as YouTubeInfo
        val vGet = VGet(videoInfo, path.toFile())
        vGet.download(stop) {
            when (videoInfo.state) {
                VideoInfo.States.ERROR -> {
                    throw videoInfo.exception
                }
                VideoInfo.States.DOWNLOADING -> {
                    val parts = videoInfo.info.filter { it.state == URLInfo.States.DOWNLOADING }
                    val current = parts.map { it.count }.sum()
                    val max = parts.map { it.length }.sum()
                    if (!parts.isEmpty()) {
                        progress?.notify(current, max, 100.0 * current / max)
                    }
                }
                else -> {
                    // do nothing
                }
            }
        }

        return map(videoInfo.info.map { it.targetFile }, cached = false)
    }
}
