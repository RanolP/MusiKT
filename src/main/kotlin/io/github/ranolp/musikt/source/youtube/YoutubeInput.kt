package io.github.ranolp.musikt.source.youtube

import io.github.ranolp.musikt.YOUTUBE_LONG
import io.github.ranolp.musikt.YOUTUBE_SHORT

data class YoutubeInput internal constructor(val id: String) {
    companion object {
        @JvmStatic
        fun url(url: String): YoutubeInput {
            for (type in listOf(YOUTUBE_SHORT, YOUTUBE_LONG)) {
                if (!type.matches(url)) {
                    continue
                }
                type.replace(url, "$1").takeIf { it.isNotEmpty() }?.let {
                    return YoutubeInput(it)
                }
            }
            throw IllegalArgumentException("$url is not valid youtube url")
        }

        @JvmStatic
        fun id(id: String): YoutubeInput {
            return YoutubeInput(id)
        }
    }
}
