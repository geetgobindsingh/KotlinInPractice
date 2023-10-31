package extentionfun

import kotlin.reflect.full.defaultType
import kotlin.reflect.typeOf


class Basic {
    var a = 1
    fun doNothing() {}
}

fun Basic.doSomething() {
    a = 2
    println("did something")
}

fun main() {
    val basic = Basic()
    basic.doNothing()
    basic.doSomething()

    val a = listOf(null)
    println(a::class.typeParameters)
}