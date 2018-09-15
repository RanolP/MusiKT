package io.github.ranolp.musikt.source.soundcloud

import io.github.ranolp.musikt.source.Connection
import java.net.HttpURLConnection
import java.net.URL

data class SoundCloudConnection(val userUrl: String) : Connection {
    companion object {
        private const val serialVersionUID = 4974420876435803040L
    }

    override val isAvailable: Boolean by lazy {
        val connection = URL(userUrl).openConnection() as HttpURLConnection
        connection.connect()
        when (connection.responseCode / 100) {
            2, 3 -> true
            else -> false
        }
    }
}
