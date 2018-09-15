package io.github.ranolp.musikt.source.soundcloud

import com.google.gson.JsonObject
import io.github.ranolp.musikt.source.SourceGenerator

object SoundCloudSourceGenerator : SourceGenerator<SoundCloudData>("SoundCloud") {
    override fun generate(input: JsonObject): SoundCloudSource = SoundCloudSource(input["url"].asString)
}
