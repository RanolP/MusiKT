package io.github.ranolp.musikt.util

interface Progress {
    fun notify(current: Long, all: Long, percentage: Double)

    fun complete(all: Long) {
        notify(all, all, 100.0)
    }
}
