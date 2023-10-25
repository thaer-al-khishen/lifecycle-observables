package com.relatablecode.lifecycleobservables_core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Represents a lifecycle-aware observable that provides access to its current value
 * and allows observing changes to that value in relation to Android lifecycle events.
 *
 * @property initialValue A lambda function that provides the initial value of the observer.
 * @property onChange A lambda function that is invoked when the value changes.
 * @property callbacks A set of callbacks corresponding to lifecycle events.
 * @property instantiatedAt The lifecycle event at which this observer is instantiated.
 * @property destroyedAt The lifecycle event at which this observer is destroyed.
 * @property shouldSurviveConfigurationChange Determines whether the observer should retain its value across configuration changes.
 *
 * @constructor Initializes the observer with the provided parameters.
 */
open class LifecycleAwareObserver<T>(
    var initialValue: (() -> T)? = null,
    var onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    private var callbacks: LifecycleEventCallbacks = LifecycleEventCallbacks(),
    private var instantiatedAt: Lifecycle.Event = Lifecycle.Event.ON_START,
    protected var destroyedAt: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    private var shouldSurviveConfigurationChange: Boolean = true
) : ReadOnlyProperty<Any?, T?>, LifecycleAwareObservable<T> {

    /**
     * Contains the list of observers that are monitoring this lifecycle-aware observable.
     */
    protected val observers = mutableListOf<(old: T?, new: T?) -> Unit>()

    /**
     * A lock to ensure thread safety when accessing or modifying the `_value`.
     */
    private val reentrantLock = ReentrantLock()

    /**
     * Internal backing property to store the current value.
     */
    private var _value: T? = null

    /**
     * Keeps track of the last lifecycle event that was observed.
     */
    private var lastEvent: Lifecycle.Event? = null

    init {
        _value = initialValue?.invoke()
        if (shouldSurviveConfigurationChange) {
            initialValue = null //To avoid a memory leak
        }
    }

    /**
     * Observes the given lifecycle and triggers appropriate lifecycle event callbacks.
     *
     * @param lifecycle The lifecycle to be observed.
     * @param observer The observer that's notified of changes.
     */
    override fun observe(lifecycle: Lifecycle, observer: (old: T?, new: T?) -> Unit) {
        observers.add(observer)
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                lastEvent = event
                handleLifecycleEvent(event)
                if (event == Lifecycle.Event.ON_DESTROY) {
                    callbacks.onDestroy.invoke()
                    maybeDestroy(event)
                    observers.remove(observer)
                    lifecycle.removeObserver(this)
                }
            }
        })
    }

    /**
     * Handles the various lifecycle events and invokes the appropriate callbacks.
     *
     * @param event The lifecycle event to handle.
     */
    private fun handleLifecycleEvent(event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                callbacks.onCreate.invoke()
                maybeInstantiate(event)
            }
            Lifecycle.Event.ON_START -> {
                callbacks.onStart.invoke()
                maybeInstantiate(event)
            }
            Lifecycle.Event.ON_RESUME -> {
                callbacks.onResume.invoke()
                maybeInstantiate(event)
            }
            Lifecycle.Event.ON_PAUSE -> {
                callbacks.onPause.invoke()
                maybeDestroy(event)
            }
            Lifecycle.Event.ON_STOP -> {
                callbacks.onStop.invoke()
                maybeDestroy(event)
            }
            else -> { /* No-op */ }
        }
    }

    /**
     * Returns the current value of the observer. If the observer's lifecycle is destroyed, it returns null.
     *
     * @param thisRef The reference to the object that holds the delegated property.
     * @param property The metadata of the delegated property.
     * @return The current value of the observer or null.
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return reentrantLock.withLock {
            if (lastEvent == destroyedAt) {
                null
            } else {
                _value
            }
        }
    }

    /**
     * Represents the current value of the observer.
     */
    override var value: T?
        get() = _value
        set(newValue) {
            val oldValue = _value
            _value = newValue
            onChange.invoke(oldValue, newValue)
        }

    /**
     * If the lifecycle event matches `instantiatedAt`, this method assigns the `initialValue` to the observer's value.
     *
     * @param event The lifecycle event to check against.
     */
    private fun maybeInstantiate(event: Lifecycle.Event) {
        if (instantiatedAt == event && value == null) {
            value = initialValue?.invoke()
        }
    }

    /**
     * If the lifecycle event matches `destroyedAt` and `shouldSurviveConfigurationChange` is false, this method nullifies the observer's value.
     *
     * @param event The lifecycle event to check against.
     */
    private fun maybeDestroy(event: Lifecycle.Event) {
        if (destroyedAt == event && !shouldSurviveConfigurationChange) {
            value = null
        }
    }

    /**
     * A builder class to simplify the construction of a `LifecycleAwareObserver`.
     */
    class Builder<T> {
        private var initialValue: (() -> T)? = null
        private var onChange: (old: T?, new: T?) -> Unit = { _, _ -> }
        private var callbacks = LifecycleEventCallbacks()
        private var instantiatedAt = Lifecycle.Event.ON_START
        private var destroyedAt = Lifecycle.Event.ON_STOP
        private var shouldSurviveConfigurationChange = true

        /**
         * Sets the initial value provider for the observer.
         *
         * @param initialValue A lambda that returns the initial value.
         * @return This builder instance.
         */
        fun setInitialValue(initialValue: () -> T) = apply { this.initialValue = initialValue }
        fun setOnChange(onChange: (old: T?, new: T?) -> Unit) = apply { this.onChange = onChange }
        fun setCallbacks(callbacks: LifecycleEventCallbacks) = apply { this.callbacks = callbacks }
        fun setInstantiatedAt(event: Lifecycle.Event) = apply { this.instantiatedAt = event }
        fun setDestroyedAt(event: Lifecycle.Event) = apply { this.destroyedAt = event }
        fun setShouldSurviveConfigurationChange(value: Boolean) = apply { this.shouldSurviveConfigurationChange = value }

        /**
         * Constructs the `LifecycleAwareObserver` with the specified configurations.
         *
         * @return A new `LifecycleAwareObserver` instance.
         */
        fun build(): LifecycleAwareObserver<T> {
            return LifecycleAwareObserver(initialValue, onChange, callbacks, instantiatedAt, destroyedAt, shouldSurviveConfigurationChange)
        }
    }

}
