package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

//Keeping Only the Latest Element
/*
  Conflated Channel is a type of channel that can hold only one element at a time,
  and if a new element is sent before the previous one is consumed,
  the channel will overwrite the previous element with the new one,
  effectively “conflating” or merging the two.
 */
class ConflateChannel {
    companion object {
        const val TAG = "ConflateChannel"
    }
    fun conflatedChannel(coroutineScope: CoroutineScope) {
        val channel = Channel<Int>(Channel.CONFLATED)
        coroutineScope.launch {
            for (i in 1..5) {
                Logger.d(TAG, "send: $i")
                channel.send(i) // send data to the channel
            }
            channel.close()
        }

        coroutineScope.launch {
            // iterate over the channel until it's closed
            for (value in channel) {
                Logger.d("Consumer Received $value")
            }
            coroutineScope.cancel()
        }
    }
}

fun main(args: Array<String>) = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    ConflateChannel().conflatedChannel(coroutineScope)
    while (coroutineScope.isActive) { }
    Logger.d(ConflateChannel.TAG,"coroutineScope: Closed")
}