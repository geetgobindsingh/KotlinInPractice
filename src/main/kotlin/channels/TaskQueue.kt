package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach

class TaskQueue {
    val taskChannel = Channel<() -> Unit>(Channel.UNLIMITED)

    var count = 0

    suspend fun taskProducer(taskChannel: Channel<() -> Unit>) {
        // Generate a task and send it to the channel
        val task = { println("Executing task ${count++}") }
        taskChannel.send(task)
    }

    // receives tasks from the producer and executes them
    suspend fun taskWorker(taskChannel: Channel<() -> Unit>) {
        taskChannel.consumeEach { task ->
            // Execute the task
            task()
        }
    }

}

fun main(args: Array<String>) = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val tq = TaskQueue()
    coroutineScope.launch {
        repeat(5) { launch { tq.taskProducer(tq.taskChannel) } }
        launch { tq.taskWorker(tq.taskChannel) }
    }
    while (coroutineScope.isActive) { }
    Logger.d(Pipeline.TAG,"coroutineScope: Closed")
}