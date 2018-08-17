package io.github.ranolp.musikt.source.youtube

import com.google.gson.JsonObject
import io.github.ranolp.musikt.source.SourceGenerator

object YoutubeSourceGenerator : SourceGenerator<YoutubeData>() {
    override val serviceName: String = "Youtube"

    override fun generate(input: JsonObject): YoutubeSource = YoutubeSource(input["id"].asString)
}
