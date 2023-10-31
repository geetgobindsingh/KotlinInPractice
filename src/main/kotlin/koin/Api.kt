package koin

fun getLiteKoin() = LiteKoinContext.getLiteKoin()


inline fun <reified T: Any> get(): T {
    val service = getLiteKoin().resolveInstance(T::class)
    return service.instance as T
}


inline fun <reified T: Any> inject(): Lazy<T> = lazy { get<T>() }


fun module(block: Module.() -> Unit) = Module().apply(block)

