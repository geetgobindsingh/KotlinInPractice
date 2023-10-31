package channels

import Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

class UnlimitedChannel {
    companion object {
        const val TAG = "UnlimitedChannel"
    }
    fun unlimitedChannel(coroutineScope : CoroutineScope) {
        val channel = Channel<Int>(Channel.UNLIMITED, onBufferOverflow = BufferOverflow.DROP_OLDEST)

        coroutineScope.launch {
            for (i in 1..5) {
                Logger.d(TAG, "send: $i")
                channel.send(i) // send data to the channel
                //delay(1)
                yield()
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
    UnlimitedChannel().unlimitedChannel(coroutineScope)
    while (coroutineScope.isActive) { }
    Logger.d(Pipeline.TAG,"coroutineScope: Closed")
}