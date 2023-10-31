import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
//
//class CustomChannelUsingSequence<T> {
//    private val values = mutableListOf<T>()
//    private val mutex = Mutex()
//
//    suspend fun emit(value: T) {
//        withContext(Dispatchers.IO) {
//            mutex.withLock {
//                values.add(value)
//            }
//        }
//    }
//
//    suspend fun toSequence(): Sequence<T> {
//        return sequence {
//            while (true) {
//                val value: T?
//                mutex.withLock {
//                    value = if (values.isNotEmpty()) values.removeAt(0) else null
//                }
//                if (value != null) {
//                    yield(value)
//                } else {
//                    // Add your own logic to decide when to stop yielding
//                    // For example, you could use a flag or some condition.
//                }
//            }
//        }
//    }
//}
//
//fun main() = runBlocking {
//    val customEmitter = CustomChannelUsingSequence<Int>()
//    val sequence = customEmitter.toSequence()
//
//    // Launch the emitting coroutine in a background context
//    launch(Dispatchers.IO) {
//        for (i in 1..10) {
//            customEmitter.emit(i)
//            delay(1000)
//        }
//    }
//
//    // Consume the sequence in the main/UI thread
//    launch(Dispatchers.Main) {
//        for (value in sequence) {
//            println("Received: $value")
//        }
//    }
//}
