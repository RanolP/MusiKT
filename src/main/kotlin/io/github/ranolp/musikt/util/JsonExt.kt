package io.github.ranolp.musikt.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import java.io.InputStream
import java.io.Reader

val GSON = Gson()
val JSON_PARSER = JsonParser()

fun String.parseJson(): JsonElement = JSON_PARSER.parse(this)

fun InputStream.parseJson(): JsonElement = JSON_PARSER.parse(reader())

fun Reader.parseJson(): JsonElement = JSON_PARSER.parse(this)

fun JsonReader.parseJson(): JsonElement = JSON_PARSER.parse(this)

inline fun <reified T> JsonElement.convertTo(): T = GSON.fromJson(this, T::class.java)

fun Any?.serializeJson(): JsonElement = GSON.toJson(this).parseJson()
