package io.github.ranolp.musikt.source.soundcloud

import io.github.ranolp.musikt.SOUNDCLOUD

data class SoundCloudInput internal constructor(val url: String) {
    companion object {
        @JvmStatic
        fun url(url: String): SoundCloudInput {
            for (type in listOf(SOUNDCLOUD)) {
                if (!type.matches(url)) {
                    continue
                }
                return SoundCloudInput(url)
            }
            throw IllegalArgumentException("$url is not valid sound cloud url")
        }
    }
}
