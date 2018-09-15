package io.github.ranolp.musikt.source

import com.google.gson.JsonObject
import io.github.ranolp.musikt.source.soundcloud.SoundCloudSourceGenerator
import io.github.ranolp.musikt.source.youtube.YoutubeSourceGenerator

abstract class SourceGenerator<Output : SourceData>(val serviceName: String) {
    companion object {
        private val generatorMap = mutableMapOf<String, SourceGenerator<*>>()

        val generators: Set<SourceGenerator<*>>
            get() = generatorMap.values.toSet()

        fun register(generator: SourceGenerator<*>) {
            generatorMap[generator.serviceName] = generator
        }

        fun of(name: String): SourceGenerator<*>? = generatorMap[name]

        init {
            register(SoundCloudSourceGenerator)
            register(YoutubeSourceGenerator)
        }
    }

    abstract fun generate(input: JsonObject): Source<Output>
}
