package com.relatablecode.lifecycleobservables

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class LifecycleAwareObserver<T>(
    private var initialValue: (() -> T)? = null,
    val onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    private val onCreate: () -> Unit = {},
    private val onStart: () -> Unit = {},
    private val onResume: () -> Unit = {},
    private val onPause: () -> Unit = {},
    private val onStop: () -> Unit = {},
    private val onDestroy: () -> Unit = {},
    private val instantiatedAt: Lifecycle.Event = Lifecycle.Event.ON_START,
    private val destroyedAt: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    private val shouldSurviveConfigurationChange: Boolean = true
) : ReadOnlyProperty<Any?, T?>, LifecycleAwareObservable<T> {

    val observers = mutableListOf<(old: T?, new: T?) -> Unit>()
    private val reentrantLock = ReentrantLock()
    private var _value: T? = null
    private var lastEvent: Lifecycle.Event? = null

    init {
        _value = initialValue?.invoke()
        if (shouldSurviveConfigurationChange) {
            initialValue = null //To avoid a memory leak
        }
    }

    override fun observe(lifecycle: Lifecycle, observer: (old: T?, new: T?) -> Unit) {
        observers.add(observer)
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                lastEvent = event
                handleLifecycleEvent(event)
                if (event == Lifecycle.Event.ON_DESTROY) {
                    onDestroy.invoke()
                    maybeDestroy(event)
                    observers.remove(observer)
                    lifecycle.removeObserver(this)
                }
            }
        })
    }

    private fun handleLifecycleEvent(event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                onCreate.invoke()
                maybeInstantiate(event)
            }
            Lifecycle.Event.ON_START -> {
                onStart.invoke()
                maybeInstantiate(event)
            }
            Lifecycle.Event.ON_RESUME -> {
                onResume.invoke()
                maybeInstantiate(event)
            }
            Lifecycle.Event.ON_PAUSE -> {
                onPause.invoke()
                maybeDestroy(event)
            }
            Lifecycle.Event.ON_STOP -> {
                onStop.invoke()
                maybeDestroy(event)
            }
            else -> { /* No-op */ }
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return reentrantLock.withLock {
            if (lastEvent == destroyedAt) {
                null
            } else {
                value
            }
        }
    }

    override var value: T? = this._value

    private fun maybeInstantiate(event: Lifecycle.Event) {
        if (instantiatedAt == event && value == null) {
            value = initialValue?.invoke()
        }
    }

    private fun maybeDestroy(event: Lifecycle.Event) {
        if (destroyedAt == event && !shouldSurviveConfigurationChange) {
            value = null
        }
    }

}
