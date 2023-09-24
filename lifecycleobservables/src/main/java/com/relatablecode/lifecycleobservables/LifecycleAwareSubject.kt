package com.relatablecode.lifecycleobservables

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LifecycleAwareSubject<T>(
    private val coroutineScope: CoroutineScope,
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
    instantiatedAt: Lifecycle.Event = Lifecycle.Event.ON_START,
    destroyedAt: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    shouldSurviveConfigurationChange: Boolean = false,
    private val shouldResetFirstEmission: Boolean = false
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
    private var hasEmitted = false

    override fun setInitialValue(_initialValue: () -> T): LifecycleAwareSubject<T> {
        super.setInitialValue(_initialValue)
        return this
    }

    override fun setOnChange(_onChange: (old: T?, new: T?) -> Unit): LifecycleAwareSubject<T> {
        super.setOnChange(_onChange)
        return this
    }

    override fun setOnCreate(_onCreate: () -> Unit): LifecycleAwareSubject<T> {
        super.setOnCreate(_onCreate)
        return this
    }

    override fun setOnStart(_onStart: () -> Unit): LifecycleAwareSubject<T> {
        super.setOnStart(_onStart)
        return this
    }

    override fun setOnResume(_onResume: () -> Unit): LifecycleAwareSubject<T> {
        super.setOnResume(_onResume)
        return this
    }

    override fun setOnPause(_onPause: () -> Unit): LifecycleAwareSubject<T> {
        super.setOnPause(_onPause)
        return this
    }

    override fun setOnStop(_onStop: () -> Unit): LifecycleAwareSubject<T> {
        super.setOnStop(_onStop)
        return this
    }

    override fun setOnDestroy(_onDestroy: () -> Unit): LifecycleAwareSubject<T> {
        super.setOnDestroy(_onDestroy)
        return this
    }

    override fun setInstantiatedAt(_instantiatedAt: Lifecycle.Event): LifecycleAwareSubject<T> {
        super.setInstantiatedAt(_instantiatedAt)
        return this
    }

    override fun setDestroyedAt(_destroyedAt: Lifecycle.Event): LifecycleAwareSubject<T> {
        super.setDestroyedAt(_destroyedAt)
        return this
    }

    override fun setShouldSurviveConfigurationChange(_shouldSurviveConfigurationChange: Boolean): LifecycleAwareSubject<T> {
        super.setShouldSurviveConfigurationChange(_shouldSurviveConfigurationChange)
        return this
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        asyncUpdate(value)
    }

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
            UpdateCondition.NONE -> {
                true
            }
        }
    }

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

    fun update(
        newValue: T?,
        updateMode: UpdateMode = UpdateMode.ASYNC,
        updateCondition: UpdateCondition = UpdateCondition.NONE
    ) {
        when (updateMode) {
            UpdateMode.SYNC -> synchronizedUpdate(newValue, updateCondition)
            UpdateMode.ASYNC -> asyncUpdate(newValue, updateCondition)
        }
    }

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

    fun resetEmission() {
        hasEmitted = false
    }

}
