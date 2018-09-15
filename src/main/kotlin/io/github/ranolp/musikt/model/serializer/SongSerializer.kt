package io.github.ranolp.musikt.model.serializer

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonNull
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.source.SourceGenerator
import java.lang.reflect.Type

object SongSerializer : JsonSerializer<Song<*>>, JsonDeserializer<Song<*>> {
    override fun serialize(src: Song<*>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return src?.let {
            jsonObject(
                "generator" to it.generator.serviceName, "data" to it.data
            )
        } ?: jsonNull
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Song<*>? {
        return json?.let {
            val generator = SourceGenerator.of(it["generator"].string)!!
            Song(json["data"].obj, generator)
        }
    }
}
