package com.example.domainlogicandroidtest.domain.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

/**
Dispatchers.Default - for CPU intense work (e.g. sorting a big list)
Dispatchers.Main - what this will be depends on what you've added to your programs runtime dependencies (e.g. kotlinx-coroutines-android, for the UI thread in Android)
Dispatchers.Unconfined - runs coroutines unconfined on no specific thread
Dispatchers.IO - for heavy IO work (e.g. long-running database queries, network requests, etc ...)
 */
abstract class FlowUseCase<T, R>(protected open val coroutineContext: CoroutineContext = Dispatchers.IO) {

    fun prepare(input: T) = executeIOFlow(input).flowOn(coroutineContext)

    /**
     * Return a [Flow] that will be executed in the specified [CoroutineContext] ([Dispatchers.IO] by default).
     * There's no need to call to [flowOn] in subclasses.
     */
    protected abstract fun executeIOFlow(input: T): Flow<R>
}