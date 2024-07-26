package com.codeancy.runique

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.Executors
import kotlin.time.measureTime

fun main() {

    val b = Mutex(false)
    var a = 0

    val context = Executors.newSingleThreadExecutor()

    val time = measureTime {
        repeat(10000){
            context.submit(
                kotlinx.coroutines.Runnable {
                        println("Hello World! $it ${Thread.currentThread().name}")
                }
            )
        }
    }

    println(a)
    println(time)
}