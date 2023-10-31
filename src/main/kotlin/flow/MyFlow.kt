package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MyFlow<T> private constructor(private val producer: suspend ProducerScope<T>.() -> Unit) {
    private var context: CoroutineContext = Dispatchers.Unconfined + CoroutineName("Default")

    fun flowOn(context: CoroutineContext): MyFlow<T> {
        this.context = context
        return this
    }

    fun <R> map(transform: suspend (T) -> R): MyFlow<R> {
        val newProducer: suspend ProducerScope<R>.() -> Unit = {
            this@MyFlow.collect { value ->
                emit(transform(value))
            }.join()
        }

        return MyFlow(newProducer).flowOn(context)
    }

    fun filter(predicate: suspend (T) -> Boolean): MyFlow<T> {
        val newProducer: suspend ProducerScope<T>.() -> Unit = {
            this@MyFlow.collect { value ->
                if (predicate(value)) {
                    emit(value)
                }
            }.join()
        }
        return MyFlow(newProducer).flowOn(context)
    }

    fun collect(collector: suspend (T) -> Unit): Job {
        return CoroutineScope(context).launch {
            val channel = Channel<T>()
            launch {
                producer(object : ProducerScope<T> {
                    override val channel: SendChannel<T> = channel
                })
                channel.close()
            }

            for (item in channel) {
                collector(item)
            }
        }
    }

    companion object {
        fun <T> flow(producer: suspend ProducerScope<T>.() -> Unit): MyFlow<T> {
            return MyFlow(producer)
        }
    }
}

interface ProducerScope<T> {
    val channel: SendChannel<T>

    suspend fun emit(value: T) {
        channel.send(value)
    }
}

suspend fun logCoroutineName() {
    Logger.d(Thread.currentThread().name + " " + coroutineContext[CoroutineName.Key])
}


fun main() = runBlocking {
    System.setProperty("kotlinx.coroutines.debug", "on" )

    val myFlow = MyFlow.flow<Int> {
        logCoroutineName()
        emit(1)
        emit(2)
        emit(3)
        emit(4)
        emit(5)
    }
        //.flowOn(Dispatchers.IO + CoroutineName("IO A"))
        .map { value -> value * 2 }
        //.flowOn(Dispatchers.IO + CoroutineName("IO B"))
        .filter { value -> value % 2 == 0 }
        //.flowOn(Dispatchers.IO + CoroutineName("IO C"))

    val job = myFlow.collect { value ->
        logCoroutineName()
        println("Received: $value")
    }

    runBlocking {
        job.join()
    }
}