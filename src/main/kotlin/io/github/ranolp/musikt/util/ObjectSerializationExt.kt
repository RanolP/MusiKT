package io.github.ranolp.musikt.util

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.Path

// todo: 빠른 개발을 위해 사용했지만 역시 Serializable 은 좋지 않다. 추후에 Json 이나 Database 로 대체하자.
fun Serializable.serialize(path: Path) {
    ObjectOutputStream(Files.newOutputStream(path)).use {
        it.writeObject(this)
        it.flush()
    }
}

fun <T : Serializable> Path.deserialize(): T {
    return ObjectInputStream(Files.newInputStream(this)).use {
        it.readObject() as T
    }
}
