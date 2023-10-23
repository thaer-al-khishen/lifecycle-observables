package com.relatablecode.lifecycleobservables

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

/**
 * Creates a [LifecycleAwareSubject] attached to a ViewModel's lifecycle.
 *
 * @return A [LifecycleAwareSubject] associated with the ViewModel's lifecycle.
 */
fun <T> ViewModel.lifecycleAwareSubject(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    lifecycleEventCallbacks: LifecycleEventCallbacks,
    instantiatedAt: Lifecycle.Event = Lifecycle.Event.ON_START,
    destroyedAt: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        lifecycleEventCallbacks,
        instantiatedAt,
        destroyedAt,
        shouldSurviveConfigurationChange
    )
}

/**
 * Creates a [LifecycleAwareSubject] that is aware of the ViewModel's `ON_CREATE` and `ON_DESTROY` lifecycle events.
 *
 * @return A [LifecycleAwareSubject] associated with the ViewModel's lifecycle, starting at the `ON_CREATE` event and destroyed at the `ON_DESTROY` event.
 */
fun <T> ViewModel.lifecycleAwareSubjectWhenCreated(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    lifecycleEventCallbacks: LifecycleEventCallbacks,
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        lifecycleEventCallbacks,
        instantiatedAt = Lifecycle.Event.ON_CREATE,
        destroyedAt = Lifecycle.Event.ON_DESTROY,
        shouldSurviveConfigurationChange
    )
}

/**
 * Creates a [LifecycleAwareSubject] that is aware of the ViewModel's `ON_START` and `ON_STOP` lifecycle events.
 *
 * @return A [LifecycleAwareSubject] associated with the ViewModel's lifecycle, starting at the `ON_START` event and destroyed at the `ON_STOP` event.
 */
fun <T> ViewModel.lifecycleAwareSubjectWhenStarted(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    lifecycleEventCallbacks: LifecycleEventCallbacks,
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        lifecycleEventCallbacks,
        instantiatedAt = Lifecycle.Event.ON_START,
        destroyedAt = Lifecycle.Event.ON_STOP,
        shouldSurviveConfigurationChange
    )
}

/**
 * Creates a [LifecycleAwareSubject] that is aware of the ViewModel's `ON_RESUME` and `ON_PAUSE` lifecycle events.
 *
 * @return A [LifecycleAwareSubject] associated with the ViewModel's lifecycle, starting at the `ON_RESUME` event and destroyed at the `ON_PAUSE` event.
 */
fun <T> ViewModel.lifecycleAwareSubjectWhenResumed(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    lifecycleEventCallbacks: LifecycleEventCallbacks,
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        lifecycleEventCallbacks,
        instantiatedAt = Lifecycle.Event.ON_RESUME,
        destroyedAt = Lifecycle.Event.ON_PAUSE,
        shouldSurviveConfigurationChange
    )
}
