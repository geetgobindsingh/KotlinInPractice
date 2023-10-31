package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

class ReceiveChannel {
    /*
    ReceiveChannel is a type of channel that provides a way to receive data from a coroutine.
    It is used when you want to consume data from a channel without being able to send any data back to the sender.
     */
}

@OptIn(ExperimentalCoroutinesApi::class)
fun receiveChannel(
    coroutineScope: CoroutineScope
) {
    var channel: ReceiveChannel<String> = Channel()

    // Producer Coroutine
    coroutineScope.launch {
        channel = produce {
            send("A")
            send("B")
            send("C")
            send("D")
            // we don't have to close the channel explicitly
        }
    }

    // Consumer Coroutine
    coroutineScope.launch {
        channel.consumeEach {
            Logger.d("ReceiveChannel", "Received $it")
        }
        // sending back data to channel inside consumer coroutine is not possible 
        // because it is a ReceiveChannel
        // channel.send("E")

        // channel is automatically closed
        Logger.d("ReceiveChannel", "Is producer closed: ${channel.isClosedForReceive}")
        coroutineScope.cancel()
    }
}

fun main(args: Array<String>) = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    receiveChannel(coroutineScope = coroutineScope)
    while (coroutineScope.isActive) { }
    Logger.d("ReceiveChannel","coroutineScope: Closed")
}