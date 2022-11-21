package com.dezzomorf.financulator.extensions

import kotlinx.coroutines.*

suspend fun <T, V> Iterable<T>.parallelMap(func: suspend (T) -> V): Iterable<V> =
    coroutineScope {
        map { element ->
            async(Dispatchers.IO) { func(element) }
        }.awaitAll()
    }