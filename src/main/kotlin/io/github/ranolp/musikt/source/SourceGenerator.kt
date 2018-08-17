package io.github.ranolp.musikt.source

import com.google.gson.JsonObject

abstract class SourceGenerator<Output : SourceData> {
    abstract val serviceName: String

    abstract fun generate(input: JsonObject): Source<Output>
}
