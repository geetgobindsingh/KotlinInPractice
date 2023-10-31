package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach

class BufferedChannel {

    companion object {
        const val TAG = "BufferedChannel"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun bufferedChannel(
        coroutineScope: CoroutineScope
    ) {
        // create a buffered channel with capacity of 2
        val channel = Channel<Int>(capacity = 2)

        // get the starting time to display the time difference in the logs
        val startTime = System.currentTimeMillis()

        coroutineScope.launch {
            for (message in 1..5) {
                // send the message through the channel and log the message
                channel.send(message)
                log( "Producer Sent -> $message", startTime)
            }
            log("All Sent!", startTime)
            // close the channel when all messages are sent
            channel.close()
        }

        // launch a coroutine to consume messages from the channel
        coroutineScope.launch {
            // consume messages from the channel until it is closed
            channel.consumeEach { message ->
                log("Consumer Received $message", startTime)
                // if channel is not closed then add a delay of 2 seconds to simulate some processing time
                if (!channel.isClosedForReceive) {
                    delay(2000)
                }
            }
            log("Receiving Done!", startTime)
            coroutineScope.cancel()
        }
    }

    // To log the message and time
    fun log(message: String, startTime: Long) {
        val currentTime = System.currentTimeMillis()
        val diffTime = String.format("%.3f", (currentTime - startTime).toDouble() / 1000)
        Logger.d(TAG,"[$diffTime] $message")
    }
}

fun main(args: Array<String>) = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    BufferedChannel().bufferedChannel(coroutineScope)
    while (coroutineScope.isActive) { }
    Logger.d(Pipeline.TAG,"coroutineScope: Closed")
}