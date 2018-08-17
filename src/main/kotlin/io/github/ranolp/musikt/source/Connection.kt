package io.github.ranolp.musikt.source

import java.io.Serializable

interface Connection : Serializable {
    val isAvailable: Boolean
}
