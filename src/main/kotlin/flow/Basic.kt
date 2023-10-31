package flow

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.xml.bind.JAXBElement.GlobalScope

class Basic {
    fun getFlow(): Flow<Int> {
        return flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
        }
    }
}

fun main(args: Array<String>) {
    System.setProperty("kotlinx.coroutines.debug", "on" )

    runBlocking {
        println("-1  ${Thread.currentThread().name}")
        Basic().getFlow()
            .map {
                println("0  ${Thread.currentThread().name}")
                // this will run in IO thread
                it * 2
            }
            .flowOn(Dispatchers.IO + CoroutineName("IO"))
            .map {
                println("1  ${Thread.currentThread().name}")
                // this will run in Unconfined thread
                it * 2
            }
            .flowOn(Dispatchers.Unconfined + CoroutineName("Unconfined"))
            .collect {
                // this will run in main thread
                println("2  ${Thread.currentThread().name}")
        }
    }
}