package io.github.ranolp.musikt.util

class Progress(private val execute: (Double) -> Unit) {
    private var completed = false

    companion object {
        fun fromLambda(lambda: (Double) -> Unit): Progress {
            return Progress(lambda)
        }
    }

    fun notify(percentage: Double) {
        if (percentage >= 100.0) {
            if (completed) {
                return
            }
            completed = true
            execute(100.0)
        } else {
            execute(percentage)
        }
    }

    fun complete() {
        notify(100.0)
    }
}
