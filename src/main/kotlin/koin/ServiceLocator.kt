package koin

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class ServiceLocator {

    private val serviceMap: MutableMap<KClass<*>, Service> = ConcurrentHashMap()

    fun <T : Any> getService(clz: KClass<T>): Service {
        return serviceMap[clz] ?: error("Unable to find definition of $clz")
    }

    private fun addService(service: Service) {
        serviceMap[service.type] = service
    }


    fun loadModules(modules: List<Module>) {
        modules.forEach(::registerModule)
    }

    private fun registerModule(module: Module) {
        module.declarationRegistry.forEach {
            addService(it.value.toService())
        }
    }

}


