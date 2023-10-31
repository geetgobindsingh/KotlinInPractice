package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ConcurrentFlow {
    fun getFlow() = channelFlow<Int> {
        for (i in 1..5) {
            withContext(Dispatchers.IO) {
                trySend(i)
            }
            withContext(Dispatchers.Unconfined) {
                trySend(-1 * i )
            }
        }
    }
}

fun main(args: Array<String>) {
    runBlocking {
        ConcurrentFlow().getFlow().collect {
            print(it)
        }
    }
}