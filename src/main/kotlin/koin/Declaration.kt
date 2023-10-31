package koin

typealias Declaration<T> = () -> T


fun <T: Any> Declaration<T>.toService(): Service {
    val instance: T = this()
    return DefaultService.createService(instance)
}

