package com.jobsity.tvseries.util.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {
    fun main(work: suspend (() -> Unit)) =
            CoroutineScope(Dispatchers.Main).launch {
                work()
            }

    fun default(work: suspend(() ->Unit)) = CoroutineScope(Dispatchers.Default).launch {
        work()
    }
}