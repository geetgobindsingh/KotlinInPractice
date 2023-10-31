package flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class ExceptionHandling {
    fun getFlow() = flow {
        emit(1)
        emit(2)
        throw Exception("Error found a 2 number")
        emit(3)
        emit(4)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun main(args: Array<String>) {
    runBlocking {
        ExceptionHandling().getFlow()
            .map { if (it == 3) throw Exception("Error found a 3 number") else it }
            .catch {
                println(it.message)
            }
            .collect{
                println(it)
            }
    }
    println("==============")

    runBlocking {
        ExceptionHandling().getFlow()
            .map { if (it == 3) throw Exception("Error found a 3 number") else it }
            .onCompletion {
                if (it == null) {
                    // No exception occurred
                    println("Flow completed successfully")
                } else {
                    // Exception occurred, but flow continued
                    println("Flow completed with exception " + it.message)
                }
            }
            .catch {
                println(it.message)
            }
            .collect{
                println(it)
            }
    }
    println("==============")

    runBlocking {
        ExceptionHandling().getFlow()
            .flatMapConcat { value ->
                try {
                    flowOf(value)
                } catch (e: Exception) {
                    println(e.message)
                    emptyFlow() // Replace the emitted value with an empty flow
                }
            }
            .catch {
                println(it.message)
            }
            .collect { value ->
                // Process the emitted values
                println("$value")
            }
    }

    println("==============")

    runBlocking {
        ExceptionHandling().getFlow()
            .catch {
                println(it.message)
                emit(-1)
            }
            .collect { value ->
                // Process the emitted values
                println("$value")
            }
    }

    println("==============")
    val channel = channelFlow<Int> {
        trySend(1)
    }
    runBlocking {
        channel.flatMapLatest {
            try {
                ExceptionHandling().getFlow().catch { emit(-1) }
            } catch (e: Exception) {
                flowOf(-2)
            }
            }
            .collect {
                println(it)
            }
    }

}