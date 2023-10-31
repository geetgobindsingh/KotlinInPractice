package coroutines.prioritydispatcher

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

data class PriorityTaskInfo(val priority: Int, val uniqueId: Int)

data class PriorityTask(val priority: Int, val uniqueId: Int = 1, val task: suspend (PriorityTaskInfo) -> Unit) :
    Comparable<PriorityTask> {
    override fun compareTo(other: PriorityTask): Int {
        return this.priority.compareTo(other.priority)
    }
}


class TaskDispatcher(maxParallelCoroutines: Int = 4) {

    companion object {
        public const val TAG = "TaskDispatcher"
    }

    private val customDispatcher = Executors.newFixedThreadPool(maxParallelCoroutines).asCoroutineDispatcher()
    private val parentJob = SupervisorJob()
    private val coroutineName = CoroutineName(TAG)
    private val coroutineScope =
        CoroutineScope(parentJob + customDispatcher + coroutineName + CoroutineExceptionHandler { coroutineContext, throwable ->
            println("CoroutineExceptionHandler got $throwable")
        })
    private val set = HashSet<Int>()
    private val taskQueue = PriorityBlockingQueue<PriorityTask>()
    private val taskId = AtomicInteger(0)
    private val semaphore = Semaphore(1)


    //    init {
//        coroutineScope.launch {
//            while (taskQueue.size > 0) {
//                launch {
//                    try {
//                        semaphore.acquire()
//                        println("lock acquired " + taskQueue.size)
//                        try {
//                            taskQueue.poll()?.let {
//                                it.task(PriorityTaskInfo(it.priority, it.uniqueId))
//                                set.remove(it.uniqueId)
//                            }
//                        } catch (e: Exception) {
//                            throw CancellationException("error", e)
//                        } finally {
//                            semaphore.release()
//                            println("lock released")
//                            cancel()
//                        }
//                    } catch (e: Exception) {
//                        println(e)
//                    }
//                }
//
//            }
//        }
//    }
    init {
        coroutineScope.launch {
            while (true) {
                val task = taskQueue.poll()
                    ?: // No more tasks in the queue, exit the loop
                    break // Remove a task from the queue

                launch {
                    try {
                        semaphore.acquire()
                        println("lock acquired " + taskQueue.size)
                        try {
                            task.task(PriorityTaskInfo(task.priority, task.uniqueId))
                            set.remove(task.uniqueId)
                        } catch (e: Exception) {
                            throw CancellationException("error", e)
                        } finally {
                            semaphore.release()
                            println("lock released")
                        }
                    } catch (e: Exception) {
                        println(e)
                    }
                }
            }
        }
    }


    fun addTask(priority: Int = 1, uniqueId: Int = -1, task: suspend (PriorityTaskInfo) -> Unit) = runCatching {
        if (priority < 1) {
            throw Exception("priority must be greater or equal to 1")
        }
        var uID = uniqueId
        if (uniqueId == -1) {
            try {
                uID = taskId.incrementAndGet()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println(uID)
            }
        }
        if (set.contains(uID)) {
            throw Exception("Task Already Exist!")
        } else {
            set.add(uID)
        }

        val priorityTask = PriorityTask(priority, uID, task)
        taskQueue.offer(priorityTask)
    }

    fun cancel() {
        coroutineScope.cancel()
        (customDispatcher.executor as ExecutorService).shutdown()
    }

    fun isCompleted(): Boolean {
        return parentJob.isCompleted
    }
}


fun main() {
    val dispatcher = TaskDispatcher()

    dispatcher.addTask(1, uniqueId = 1) {
        println("Task 1 completed with unique Id: " + it.uniqueId)
        delay(2000)
        //throw Exception("wee")
    }.onFailure {
        println("Error : ${it.localizedMessage}")
    }

    Thread.sleep(3000)

    dispatcher.addTask(3, uniqueId = 2) {
        delay(500)
        println("Task 2 completed with unique Id: " + it.uniqueId)
    }.onFailure {
        println("Error : ${it.localizedMessage}")
    }
    dispatcher.addTask(2, uniqueId = 3) {
        delay(1000)
        println("Task 3 completed with unique Id: " + it.uniqueId)
    }.onFailure {
        println("Error : ${it.localizedMessage}")
    }

    // Use a while loop to keep the program running until all tasks are completed
    while (!dispatcher.isCompleted()) {
        // You can add some delay here to avoid busy-waiting
        Thread.sleep(1)
    }

    // Don't forget to cancel the dispatcher
    dispatcher.cancel()
}
