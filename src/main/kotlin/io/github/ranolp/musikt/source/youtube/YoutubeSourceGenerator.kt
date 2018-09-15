package io.github.ranolp.musikt.source.youtube

import com.google.gson.JsonObject
import io.github.ranolp.musikt.source.SourceGenerator

object YoutubeSourceGenerator : SourceGenerator<YoutubeData>("Youtube") {
    override fun generate(input: JsonObject): YoutubeSource = YoutubeSource(input["id"].asString)
}
