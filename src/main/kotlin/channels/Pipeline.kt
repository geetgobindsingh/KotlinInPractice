package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach

class Pipeline {
    companion object {
        val TAG = "Pipeline"
    }

    fun streamingNumbers(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            val numbers = produceNumbers(10)
            val result = pipeline(numbers)

            Logger.d(TAG, result.receive().toString())
            coroutineScope.cancel()
        }
    }

    // Producing numbers, each number being sent to the pipeline
    fun CoroutineScope.produceNumbers(count: Int): ReceiveChannel<Int> = produce {
        for (i in 1..count) send(i)
    }

    // Pipeline which process the numbers
    fun CoroutineScope.pipeline(
        numbers: ReceiveChannel<Int>
    ): ReceiveChannel<Int> = produce {
        // Filtering out even numbers
        val filtered = filter(numbers) { it % 2 != 0 }

        // Squaring the remaining odd numbers
        val squared = map(filtered) { it * it }

        // Summing them up
        val sum = reduce(squared) { acc, x -> acc + x }

        send(sum)
    }

    fun CoroutineScope.filter(
        numbers: ReceiveChannel<Int>,
        predicate: (Int) -> Boolean
    ): ReceiveChannel<Int> = produce {
        numbers.consumeEach { number ->
            if (predicate(number)) send(number)
        }
    }

    fun CoroutineScope.map(
        numbers: ReceiveChannel<Int>,
        mapper: (Int) -> Int
    ): ReceiveChannel<Int> = produce {
        numbers.consumeEach { number ->
            send(mapper(number))
        }
    }

    fun reduce(
        numbers: ReceiveChannel<Int>,
        accumulator: (Int, Int) -> Int
    ): Int = runBlocking {
        var result = 0
        for (number in numbers) {
            Logger.d(TAG,"number: $number")
            result = accumulator(result, number)
        }
        result
    }
}

fun main(args: Array<String>) = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    Pipeline().streamingNumbers(coroutineScope)
    while (coroutineScope.isActive) { }
    Logger.d(Pipeline.TAG,"coroutineScope: Closed")
}