package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class RendezvousChannel {
    companion object {
        val TAG = "RendezvousChannel"
    }

    fun rendezvousChannel(
        coroutineScope: CoroutineScope
    ) {
        // create a rendezvous channel with capacity 0
        val channel = Channel<Int>(Channel.RENDEZVOUS)

        // get the starting time to display the time difference in the Loggers
        val startTime = System.currentTimeMillis()

        // launch the producer coroutine
        coroutineScope.launch {
            for (i in 1..5) {
                Logger( "Producer -> Sending $i", startTime)
                channel.send(i) // send data to the channel
                Logger( "Producer -> Sent $i", startTime)
                //delay(1)
                yield()
            }
            channel.close() // close the channel after sending all data
        }

        // launch the consumer coroutine
        coroutineScope.launch {
            // iterate over the channel until it's closed
            for (value in channel) {
                Logger("Consumer Received $value", startTime)
            }
            coroutineScope.cancel()
        }
    }

    // To Logger the message and time
    fun Logger(message: String, startTime: Long) {
        val currentTime = System.currentTimeMillis()
        val diffTime = String.format("%.3f", (currentTime - startTime).toDouble() / 1000)
        Logger.d(TAG,"[$diffTime] $message")
    }
}

fun main(args: Array<String>) = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    RendezvousChannel().rendezvousChannel(coroutineScope)
    while (coroutineScope.isActive) { }
    Logger.d(Pipeline.TAG,"coroutineScope: Closed")
}