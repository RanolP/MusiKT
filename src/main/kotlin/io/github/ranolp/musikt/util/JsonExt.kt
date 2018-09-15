package io.github.ranolp.musikt.util

import com.github.salomonbrys.kotson.typeToken
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import io.github.ranolp.musikt.model.Playlist
import io.github.ranolp.musikt.model.Song
import io.github.ranolp.musikt.model.serializer.PlaylistSerializer
import io.github.ranolp.musikt.model.serializer.SongSerializer
import java.io.InputStream
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path

val GSON = GsonBuilder().registerTypeAdapter(typeToken<Playlist>(), PlaylistSerializer)
        .registerTypeAdapter(typeToken<Song<*>>(), SongSerializer).create()
val JSON_PARSER = JsonParser()

fun String.parseJson(): JsonElement = JSON_PARSER.parse(this)

fun InputStream.parseJson(): JsonElement = JSON_PARSER.parse(reader())

fun Reader.parseJson(): JsonElement = JSON_PARSER.parse(this)

fun JsonReader.parseJson(): JsonElement = JSON_PARSER.parse(this)

fun Path.parseJson(ignoreError: Boolean = true): JsonElement? {
    return if (toFile().exists()) {
        try {
            JSON_PARSER.parse(Files.newBufferedReader(this))
        } catch (th: Throwable) {
            if (ignoreError) {
                null
            } else {
                throw th
            }
        }
    } else {
        null
    }
}

inline fun <reified T> JsonElement.convertTo(): T = GSON.fromJson(this, T::class.java)

fun Any?.serializeJson(): JsonElement = GSON.toJson(this).parseJson()

fun JsonElement.stringfy(): String = GSON.toJson(this)

fun JsonElement.saveInto(path: Path) {
    stringfy().writeInto(path)
}

fun String.writeInto(path: Path) {
    if (!path.toFile().exists()) {
        Files.createDirectories(path.parent)
        Files.createFile(path)
    }
    Files.write(path, toByteArray())
}
