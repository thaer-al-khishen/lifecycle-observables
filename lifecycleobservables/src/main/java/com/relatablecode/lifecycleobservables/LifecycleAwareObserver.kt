package com.relatablecode.lifecycleobservables

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class LifecycleAwareObserver<T>(
    var initialValue: (() -> T)? = null,
    var onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    private var onCreate: () -> Unit = {},
    private var onStart: () -> Unit = {},
    private var onResume: () -> Unit = {},
    private var onPause: () -> Unit = {},
    private var onStop: () -> Unit = {},
    private var onDestroy: () -> Unit = {},
    private var instantiatedAt: Lifecycle.Event = Lifecycle.Event.ON_START,
    private var destroyedAt: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    private var shouldSurviveConfigurationChange: Boolean = true
) : ReadOnlyProperty<Any?, T?>, LifecycleAwareObservable<T> {

    protected val observers = mutableListOf<(old: T?, new: T?) -> Unit>()
    private val reentrantLock = ReentrantLock()
    private var _value: T? = null
    private var lastEvent: Lifecycle.Event? = null

    init {
        _value = initialValue?.invoke()
        if (shouldSurviveConfigurationChange) {
            initialValue = null //To avoid a memory leak
        }
    }

    open fun setInitialValue(_initialValue: () -> T): LifecycleAwareObserver<T> {
        this.initialValue = _initialValue
        return this
    }

    open fun setOnChange(_onChange: (old: T?, new: T?) -> Unit): LifecycleAwareObserver<T> {
        this.onChange = _onChange
        return this
    }

    open fun setOnCreate(_onCreate: () -> Unit): LifecycleAwareObserver<T> {
        this.onCreate = _onCreate
        return this
    }

    open fun setOnStart(_onStart: () -> Unit): LifecycleAwareObserver<T> {
        this.onStart = _onStart
        return this
    }

    open fun setOnResume(_onResume: () -> Unit): LifecycleAwareObserver<T> {
        this.onResume = _onResume
        return this
    }

    open fun setOnPause(_onPause: () -> Unit): LifecycleAwareObserver<T> {
        this.onPause = _onPause
        return this
    }

    open fun setOnStop(_onStop: () -> Unit): LifecycleAwareObserver<T> {
        this.onStop = _onStop
        return this
    }

    open fun setOnDestroy(_onDestroy: () -> Unit): LifecycleAwareObserver<T> {
        this.onDestroy = _onDestroy
        return this
    }

    open fun setInstantiatedAt(_instantiatedAt: Lifecycle.Event): LifecycleAwareObserver<T> {
        this.instantiatedAt = _instantiatedAt
        return this
    }

    open fun setDestroyedAt(_destroyedAt: Lifecycle.Event): LifecycleAwareObserver<T> {
        this.destroyedAt = _destroyedAt
        return this
    }

    open fun setShouldSurviveConfigurationChange(_shouldSurviveConfigurationChange: Boolean): LifecycleAwareObserver<T> {
        this.shouldSurviveConfigurationChange = _shouldSurviveConfigurationChange
        return this
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
