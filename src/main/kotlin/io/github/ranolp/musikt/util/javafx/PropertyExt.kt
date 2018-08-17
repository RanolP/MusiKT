package io.github.ranolp.musikt.util.javafx

import javafx.beans.property.ReadOnlyObjectPropertyBase
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.ReadOnlyStringPropertyBase

fun <T> readOnly(name: String, bean: Any? = null, get: () -> T): ReadOnlyProperty<T> = object :
        ReadOnlyObjectPropertyBase<T>() {
    override fun getName(): String = name

    override fun getBean(): Any? = bean

    override fun get(): T = get()
}

fun readOnly(name: String, bean: Any? = null, get: () -> String): ReadOnlyStringProperty = object :
        ReadOnlyStringPropertyBase() {
    override fun getName(): String = name

    override fun getBean(): Any? = bean

    override fun get(): String = get()
}
