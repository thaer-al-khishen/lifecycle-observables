package com.relatablecode.lifecycleobservables

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

fun <T> ViewModel.lifecycleAwareSubject(
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
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
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
    )
}

fun <T> ViewModel.lifecycleAwareSubjectWhenCreated(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        onCreate,
        onStart,
        onResume,
        onPause,
        onStop,
        onDestroy,
        instantiatedAt = Lifecycle.Event.ON_CREATE,
        destroyedAt = Lifecycle.Event.ON_DESTROY,
        shouldSurviveConfigurationChange
    )
}

fun <T> ViewModel.lifecycleAwareSubjectWhenStarted(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        onCreate,
        onStart,
        onResume,
        onPause,
        onStop,
        onDestroy,
        instantiatedAt = Lifecycle.Event.ON_START,
        destroyedAt = Lifecycle.Event.ON_STOP,
        shouldSurviveConfigurationChange
    )
}

fun <T> ViewModel.lifecycleAwareSubjectWhenResumed(
    initialValue: (() -> T)? = null,
    onChange: (old: T?, new: T?) -> Unit = { _, _ -> },
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
    shouldSurviveConfigurationChange: Boolean = false
): LifecycleAwareSubject<T> {
    return LifecycleAwareSubject(
        viewModelScope,
        initialValue,
        onChange,
        onCreate,
        onStart,
        onResume,
        onPause,
        onStop,
        onDestroy,
        instantiatedAt = Lifecycle.Event.ON_RESUME,
        destroyedAt = Lifecycle.Event.ON_PAUSE,
        shouldSurviveConfigurationChange
    )
}
