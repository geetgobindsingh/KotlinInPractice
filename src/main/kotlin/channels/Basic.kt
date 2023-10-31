package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach


fun channel(
    producer: CoroutineScope,
    consumer: CoroutineScope
) {
    val channel = Channel<String>() // Channel exchanges a data of String type

    // Producer starts sending data inside a coroutine
    producer.launch {
        Logger.d("Channel", "Sent data 1 to channel")
        channel.send("Data 1")
        Logger.d("Channel","Sent data 2 to channel")
        channel.send("Data 2")
        channel.close() // we're done sending so channel should be closed
        producer.cancel()
    }

    // Consumer starts receiving data inside another coroutine
    consumer.launch {
        channel.consumeEach {
            Logger.d("Channel","Received: $it")
        }
        Logger.d("Channel","Closed!") // This line called when channel is closed
        consumer.cancel()
    }
}

fun main(args: Array<String>) = runBlocking {
    val producerCoroutineScope = CoroutineScope(Dispatchers.Default)
    val consumerCoroutineScope = CoroutineScope(Dispatchers.Unconfined)
    channel(producer = producerCoroutineScope, consumer = consumerCoroutineScope)
    while (consumerCoroutineScope.isActive) { }
    Logger.d("Channel","coroutineScope: Closed")
}