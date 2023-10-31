package koin

import kotlin.reflect.KClass


class LiteKoin {

    private val registry = ServiceLocator()
    lateinit var declarations: Map<KClass<*>, Declaration<Any>>

    fun loadModules(modules: List<Module>) {
        declarations = modules.declarationRegistry
        println("Registry Size === ${modules.declarationRegistry.size}")
        registry.loadModules(modules)
    }

    fun resolveInstance(type: KClass<*>) = registry.getService(type)
}
