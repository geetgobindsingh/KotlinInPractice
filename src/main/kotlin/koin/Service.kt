package koin

import kotlin.reflect.KClass


interface Service {
    val type: KClass<*>
    val instance: Any
}


class DefaultService(
    override val type: KClass<*>,
    override val instance: Any
) : Service {

    companion object {
        fun createService(instance: Any) = DefaultService(instance::class, instance)
    }
}