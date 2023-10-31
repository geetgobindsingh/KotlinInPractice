package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FlowToSharedFlow {
    fun getFlow(coroutineScope: CoroutineScope) =  flow {
        emit(1)
        emit(2)
        emit(3)
        delay(50)
        coroutineScope.cancel()
    }
}

fun main(args: Array<String>) {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    // 1
    val sharedFlow = FlowToSharedFlow().getFlow(coroutineScope)
        .shareIn(coroutineScope, started = SharingStarted.Lazily)
    // 2
    sharedFlow.onEach {
        println("Emitting: $it")
    }.launchIn(coroutineScope)

    // 3
    while (coroutineScope.isActive) {
    }
}