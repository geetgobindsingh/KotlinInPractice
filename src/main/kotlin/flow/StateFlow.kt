package flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StateFlow {
    fun getFlow() = MutableStateFlow("A")
}

fun main(args: Array<String>) {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val stateFlow = StateFlow().getFlow()
    println("0 ${stateFlow.value}") // 1

    coroutineScope.launch {
        stateFlow.collect { // 2
            println("1 $it")
        }
    }
    stateFlow.tryEmit("B") // 2
    coroutineScope.launch {
        stateFlow.emit("C") // 3
    }
    coroutineScope.launch {
        stateFlow.collect { // 2
            println("2 $it")
        }
    }
}