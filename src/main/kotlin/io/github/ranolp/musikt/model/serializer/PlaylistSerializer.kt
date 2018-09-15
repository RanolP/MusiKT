package io.github.ranolp.musikt.model.serializer

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.typeToken
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import io.github.ranolp.musikt.model.Playlist
import io.github.ranolp.musikt.model.Song
import java.lang.reflect.Type

object PlaylistSerializer : JsonSerializer<Playlist>, JsonDeserializer<Playlist> {
    override fun serialize(src: Playlist?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
        return src?.let {
            context.serialize(it.songs)
        } ?: jsonArray()
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): Playlist? {
        return json?.let { it ->
            Playlist().apply {
                context.deserialize<List<Song<*>>>(it.array, typeToken<List<Song<*>>>()).forEach {
                    addSong(it)
                }
            }
        }
    }
}
