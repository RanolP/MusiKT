package io.github.ranolp.musikt.util

interface Progress {
    companion object {
        fun fromLambda(lambda: (Long, Long, Double) -> Unit): Progress {
            return object : Progress {
                override fun notify(current: Long, all: Long, percentage: Double) {
                    lambda(current, all, percentage)
                }
            }
        }
    }

    fun notify(current: Long, all: Long, percentage: Double)

    fun complete(all: Long) {
        notify(all, all, 100.0)
    }
}
