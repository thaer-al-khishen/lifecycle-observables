package com.relatablecode.lifecycleobservables_core

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Represents a lifecycle-aware subject, extending [LifecycleAwareObserver], designed to emit values and
 * handle those emissions in relation to the Android lifecycle. It provides the functionality of both
 * reading and writing values with lifecycle awareness.
 *
 * The subject can be updated either synchronously, asynchronously, or without locking. Care should
 * be taken when choosing an update method, especially in concurrent environments.
 *
 * @property coroutineScope Provides the scope in which asynchronous operations, like updating the subject, are launched.
 * @property shouldResetFirstEmission If true, resets the emission state upon certain lifecycle events, allowing the value to be emitted again.
 *
 * @constructor Creates a lifecycle-aware subject initialized with given parameters.
 */
class LifecycleAwareSubject<T>(
    private val coroutineScope: CoroutineScope,
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    callbacks: LifecycleEventCallbacks = LifecycleEventCallbacks(),
    instantiatedAt: Lifecycle.Event = LifecycleAwareHelper.defaultInstantiatedAt,
    destroyedAt: Lifecycle.Event = LifecycleAwareHelper.defaultDestroyedAt,
    shouldSurviveConfigurationChange: Boolean = LifecycleAwareHelper.defaultShouldSurviveConfigurationChange,
    private val shouldResetFirstEmission: Boolean = LifecycleAwareHelper.defaultShouldResetFirstEmission
) : LifecycleAwareObserver<T>(
    initialValue,
    onChange,
    callbacks,
    instantiatedAt,
    destroyedAt,
    shouldSurviveConfigurationChange
), ReadWriteProperty<Any?, T?>, LifecycleAwareObservable<T> {

    /**
     * A mutual exclusion primitive that protects updates to the subject's value in asynchronous environments.
     */
    private val mutex = Mutex()

    /**
     * A flag to check if the subject has already emitted a value.
     */
    private var hasEmitted = false

    /**
     * A handler to manage exceptions that might arise during value updates.
     */
    private var errorHandler: ((Exception) -> Unit)? = null

    /**
     * Sets the value of the subject, automatically triggering an asynchronous update by default.
     */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        asyncUpdate(value)
    }

    /**
     * Determines if an update to the subject should take place based on the provided conditions.
     *
     * @param oldValue The current value of the subject.
     * @param newValue The new value to be set.
     * @param updateCondition The condition that dictates when updates should occur.
     * @return Boolean indicating if the subject should be updated.
     */
    private fun shouldUpdate(
        oldValue: T?,
        newValue: T?,
        updateCondition: UpdateCondition
    ): Boolean {
        return when (updateCondition) {
            UpdateCondition.UNIQUE -> {
                oldValue != newValue
            }

            UpdateCondition.FIRST_ONLY -> {
                !hasEmitted
            }

            UpdateCondition.FIRST_ONLY_AND_NOT_NULL -> {
                !hasEmitted && newValue != null
            }

            UpdateCondition.NONE -> {
                true
            }
        }
    }

    /**
     * Updates the value of the subject asynchronously, ensuring synchronization to avoid concurrent modifications.
     *
     * @param newValue The new value to set.
     * @param updateCondition The condition under which the update should occur.
     */
    private fun asyncUpdate(newValue: T?, updateCondition: UpdateCondition = UpdateCondition.NONE) {
        val oldValue = this.value
        if (shouldUpdate(oldValue, newValue, updateCondition)) {
            hasEmitted = true
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
    }

    /**
     * Updates the value of the subject synchronously.
     *
     * @param newValue The new value to set.
     * @param updateCondition The condition under which the update should occur.
     */
    @Synchronized
    private fun synchronizedUpdate(
        newValue: T?,
        updateCondition: UpdateCondition = UpdateCondition.NONE
    ) {
        val oldValue = this.value
        if (shouldUpdate(oldValue, newValue, updateCondition)) {
            hasEmitted = true
            this@LifecycleAwareSubject.value = newValue
            onChange.invoke(oldValue, newValue)
            observers.forEach { it(oldValue, newValue) }
        }
    }

    /**
     * Updates the value of the subject without any locking mechanism.
     *
     * @param newValue The new value to set.
     * @param updateCondition The condition under which the update should occur.
     *
     * @warning This method does not ensure atomicity and data consistency in concurrent environments.
     */
    fun nonLockingUpdate(newValue: T?, updateCondition: UpdateCondition = UpdateCondition.NONE) {
        val oldValue = this.value
        if (shouldUpdate(oldValue, newValue, updateCondition)) {
            hasEmitted = true
            this@LifecycleAwareSubject.value = newValue
            onChange.invoke(oldValue, newValue)
            observers.forEach { it(oldValue, newValue) }
        }
    }

    /**
     * Provides a way to update the subject value using various modes.
     *
     * @param newValue The new value to set.
     * @param updateMode The mode (SYNC, ASYNC, NON_LOCKING) in which the update should occur.
     * @param updateCondition The condition under which the update should occur.
     */
    fun update(
        newValue: T?,
        updateMode: UpdateMode = UpdateMode.ASYNC,
        updateCondition: UpdateCondition = UpdateCondition.NONE
    ): LifecycleAwareSubject<T> {
        try {
            when (updateMode) {
                UpdateMode.SYNC -> synchronizedUpdate(newValue, updateCondition)
                UpdateMode.ASYNC -> asyncUpdate(newValue, updateCondition)
                UpdateMode.NON_LOCKING -> nonLockingUpdate(newValue, updateCondition)
            }
        } catch (e: Exception) {
            errorHandler?.invoke(e)
        }
        return this
    }

    fun onError(handler: (Exception) -> Unit) {
        errorHandler = handler
    }

    /**
     * Overrides the observe function from [LifecycleAwareObserver]. In addition to the base functionality,
     * it checks if the emission should be reset upon reaching certain lifecycle events.
     *
     * @param lifecycle The lifecycle to observe.
     * @param observer The callback triggered on value changes.
     */
    override fun observe(lifecycle: Lifecycle, observer: (old: T?, new: T?) -> Unit) {
        super.observe(lifecycle, observer)
        lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onDestroy(owner: LifecycleOwner) {
                if (destroyedAt == Lifecycle.Event.ON_DESTROY && shouldResetFirstEmission) {
                    resetEmission()
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                if (destroyedAt == Lifecycle.Event.ON_STOP && shouldResetFirstEmission) {
                    resetEmission()
                }
            }

            override fun onPause(owner: LifecycleOwner) {
                if (destroyedAt == Lifecycle.Event.ON_PAUSE && shouldResetFirstEmission) {
                    resetEmission()
                }
            }

        })
    }

    /**
     * Resets the emission state of the subject, allowing a value to be emitted again.
     */
    fun resetEmission() {
        hasEmitted = false
    }

}
