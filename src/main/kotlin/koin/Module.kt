package koin

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass



class Module {

    val declarationRegistry: MutableMap<KClass<*>, Declaration<Any>> = ConcurrentHashMap()

    inline fun <reified T: Any> factory(noinline declaration: Declaration<T>) {
        declarationRegistry[T::class] = declaration
    }


    inline fun <reified T: Any> get(): T {
        val declaration = declarationRegistry[T::class]
        var instance = declaration?.invoke()
        if (instance == null) {
            val liteKoin = LiteKoinContext.getLiteKoin()
            instance = liteKoin.declarations[T::class]?.invoke() ?: error("Unable to find declaration of type ${T::class.qualifiedName}")
        }
        return instance as T
    }


    operator fun plus(module: Module) = listOf(module, this)
}


operator fun List<Module>.plus(module: Module) = this + listOf(module)


val List<Module>.declarationRegistry: Map<KClass<*>, Declaration<Any>>
    get() = this.fold(this[0].declarationRegistry) { acc, module -> (acc + module.declarationRegistry) as MutableMap<KClass<*>, Declaration<Any>> }


