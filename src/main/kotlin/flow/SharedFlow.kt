package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SharedFlow {
    fun getFlow() = MutableSharedFlow<Int>(2)

}

fun main(args: Array<String>) {
    val sharedFlow = SharedFlow().getFlow()

    runBlocking {
        // 0
        sharedFlow.tryEmit(1)
        sharedFlow.tryEmit(2)
        sharedFlow.tryEmit(3)
        // 1
        sharedFlow.onEach {
            println("Emitting: $it")
        }.launchIn(GlobalScope)
        // 2
        sharedFlow.onEach {
            println("Hello: $it")
        }.launchIn(GlobalScope)

        //delay(10)

        // 3
        sharedFlow.tryEmit(4)
        //sharedFlow.tryEmit(5)
        // 4
        delay(10)

//        val coroutineScope = CoroutineScope(Dispatchers.Default)
//        // 5
//        coroutineScope.launch {
//            sharedFlow.emit(6)
//            sharedFlow.emit(7)
//            sharedFlow.emit(8)
//        // 6
//            coroutineScope.cancel()
//        }
//        // 7
//        while (coroutineScope.isActive) {
//        }
    }
}