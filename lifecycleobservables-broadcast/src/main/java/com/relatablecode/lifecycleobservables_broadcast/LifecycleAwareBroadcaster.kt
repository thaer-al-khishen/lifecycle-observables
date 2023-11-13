package com.relatablecode.lifecycleobservables_broadcast

import androidx.lifecycle.LifecycleOwner
import com.relatablecode.lifecycleobservables_core.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables_core.UpdateCondition
import com.relatablecode.lifecycleobservables_core.UpdateMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LifecycleAwareBroadcaster<T> private constructor(
    initialValue: T,
    coroutineScope: CoroutineScope
) {

    private val lifecycleAwareEvent: LifecycleAwareSubject<T> = LifecycleAwareSubject(
        coroutineScope = coroutineScope,
        initialValue = { initialValue }
    )

    fun broadcastEvent(event: T) {
        lifecycleAwareEvent.update(event, UpdateMode.NON_LOCKING, UpdateCondition.NONE)
    }

    fun subscribeToEvent(lifecycleOwner: LifecycleOwner, onNewValue: (T?, T?) -> Unit) {
        lifecycleAwareEvent.observe(lifecycleOwner.lifecycle) { old, new ->
            onNewValue.invoke(old, new)
        }
    }

    companion object {

        private var _instance: LifecycleAwareBroadcaster<*>? = null

        fun <T> initialize(
            initialValue: T,
            coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        ) {
            if (_instance == null) {
                _instance = LifecycleAwareBroadcaster(initialValue, coroutineScope)
            } else throw IllegalStateException("LifecycleAwareBroadcaster instance is already initialized")
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> getInstance(): LifecycleAwareBroadcaster<T> {
            return _instance as? LifecycleAwareBroadcaster<T>
                ?: throw IllegalStateException("LifecycleAwareBroadcaster is not initialized")
        }

    }

}
