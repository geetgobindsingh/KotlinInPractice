package sequences

import Logger
import channels.Pipeline
import channels.TaskQueue
import kotlinx.coroutines.*

class Basic {
    /*
    In Kotlin, a Sequence is a type of collection that represents a lazy,
     potentially infinite sequence of elements.
      It's similar to an iterator or a generator in other programming languages.
      Sequences are part of the Kotlin Standard Library and are designed to enable more efficient processing of large
      or infinite data sets by computing elements on-demand, rather than loading everything into memory at once.

    Sequences are particularly useful when you want to perform transformations and filtering on a collection
     without creating intermediate collections for each step. This can save memory and improve performance,
      especially for large data sets.
     */

    fun generateSeq(): Sequence<Int> {
        return sequence {
            var counter = 0
            while (counter < 101) {
                yield(counter)
                counter++
            }
        }
    }

}

fun main(args: Array<String>) = runBlocking {
    Basic().generateSeq().take(10).filter { i -> i % 2 == 0 }.forEach {
        Logger.d(it.toString())
    }

    val numbers = generateSequence(1) { it + 1 }
    val filteredNumbers = numbers.filter { it % 2 == 0 }
    val squaredNumbers = filteredNumbers.map { it * it }
    val firstFiveSquared = squaredNumbers.take(5).toList()
    firstFiveSquared.forEach{
        Logger.d(it.toString())
    }
}