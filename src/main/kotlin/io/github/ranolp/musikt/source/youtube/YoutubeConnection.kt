package io.github.ranolp.musikt.source.youtube

import io.github.ranolp.musikt.source.Connection
import java.net.HttpURLConnection
import java.net.URL

data class YoutubeConnection(val channelUrl: String) : Connection {
    companion object {
        private const val serialVersionUID = 2811530726686798220L
    }

    override val isAvailable: Boolean by lazy {
        val connection = URL(channelUrl).openConnection() as HttpURLConnection
        connection.connect()
        when (connection.responseCode / 100) {
            2, 3 -> true
            else -> false
        }
    }
}
