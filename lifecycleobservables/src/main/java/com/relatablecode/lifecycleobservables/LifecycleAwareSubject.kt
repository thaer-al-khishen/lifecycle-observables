package com.relatablecode.lifecycleobservables

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LifecycleAwareSubject<T>(
    initialValue: (() -> T)? = null,
    private val coroutineScope: CoroutineScope,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
    instantiatedAt: Lifecycle.Event = Lifecycle.Event.ON_START,
    destroyedAt: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    shouldSurviveConfigurationChange: Boolean = true
) : LifecycleAwareObserver<T>(
    initialValue,
    onChange,
    onCreate,
    onStart,
    onResume,
    onPause,
    onStop,
    onDestroy,
    instantiatedAt,
    destroyedAt,
    shouldSurviveConfigurationChange
), ReadWriteProperty<Any?, T?>, LifecycleAwareObservable<T> {

    private val mutex = Mutex()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        val oldValue = this.value
        coroutineScope.launch {
            mutex.withLock {
                this@LifecycleAwareSubject.value = value
                withContext(Dispatchers.Main) {
                    onChange.invoke(oldValue, value)
                    observers.forEach { it(oldValue, value) }
                }
            }
        }
    }

    fun asyncUpdate(newValue: T?) {
        val oldValue = this.value
        coroutineScope.launch {
            mutex.withLock {
                this@LifecycleAwareSubject.value = newValue
                withContext(Dispatchers.Main) {
                    onChange.invoke(oldValue, newValue)
                    observers.forEach { it(oldValue, newValue) }
                }
            }
        }
    }

    @Synchronized
    fun synchronizedUpdate(newValue: T?) {
        val oldValue = this.value
        this@LifecycleAwareSubject.value = newValue
        onChange.invoke(oldValue, newValue)
        observers.forEach { it(oldValue, newValue) }
    }

}
