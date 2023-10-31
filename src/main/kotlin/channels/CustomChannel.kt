package channels


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.omg.CORBA.Object
import kotlin.coroutines.coroutineContext

class CustomChannel<T>(private val capacity: Int) {
    private val items = mutableListOf<T>()
    private val lock = Mutex()
    private var closed = false

    suspend fun send(value: T) {
        lock.withLock {
            while (items.size >= capacity) {
                lock.unlock()
                yield()
                lock.lock()
            }
            if (!closed) {
                items.add(value)
            }

        }
    }

    suspend fun receive(): T {
        return lock.withLock {
            while (items.isEmpty() && !closed) {
                lock.unlock()
                yield()
                lock.lock()
            }
            if (!(items.isEmpty() && closed)) {
                val value = items.removeAt(0)
                value
            } else {
                coroutineContext.cancel()
                -1 as T
            }
        }
    }

    suspend fun close() {
        lock.withLock {
            closed = true
        }
    }

    fun isClosed() = closed
}

fun main() = runBlocking {
    val channel = CustomChannel<Int>(1)

    val sender = launch {
        for (i in 1..5) {
            channel.send(i)
            delay(10)
        }
        channel.close()
    }

    val receiver = launch {
        while (!channel.isClosed()) {
            val value = channel.receive()
            println("Received: $value")
        }
    }

    sender.join()
    receiver.join()
}
