package io.github.ranolp.musikt.source

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import io.github.ranolp.musikt.SOUNDCLOUD
import io.github.ranolp.musikt.YOUTUBE_ID
import io.github.ranolp.musikt.YOUTUBE_LONG
import io.github.ranolp.musikt.YOUTUBE_SHORT

val String.isValidSoundCloudUrl: Boolean
    get() = listOf(SOUNDCLOUD).any { it.matches(this) }

fun makeSoundcloudInput(url: String): JsonObject = jsonObject("url" to url)

val String.isValidYoutubeUrl: Boolean
    get() = listOf(YOUTUBE_SHORT, YOUTUBE_LONG).any { it.matches(this) }

val String.extractYoutubeId: String?
    get() = listOf(YOUTUBE_SHORT, YOUTUBE_LONG).firstOrNull {
        it.matches(this)
    }?.replace(this, "$1")?.takeIf { it.isNotEmpty() }

val String.guessYoutubeId: String?
    get() = if(YOUTUBE_ID.matches(this)) this else extractYoutubeId

fun makeYoutubeInput(id: String): JsonObject = jsonObject("id" to id)
