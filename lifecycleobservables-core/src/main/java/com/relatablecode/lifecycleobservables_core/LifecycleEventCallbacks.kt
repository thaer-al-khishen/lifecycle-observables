package com.relatablecode.lifecycleobservables_core

/**
 * Represents a set of callbacks corresponding to Android lifecycle events.
 *
 * These callbacks can be used alongside [LifecycleAwareObserver] and [LifecycleAwareSubject] to trigger specific actions at certain lifecycle events.
 *
 * @property onCreate Callback invoked during the `ON_CREATE` lifecycle event.
 * @property onStart Callback invoked during the `ON_START` lifecycle event.
 * @property onResume Callback invoked during the `ON_RESUME` lifecycle event.
 * @property onPause Callback invoked during the `ON_PAUSE` lifecycle event.
 * @property onStop Callback invoked during the `ON_STOP` lifecycle event.
 * @property onDestroy Callback invoked during the `ON_DESTROY` lifecycle event.
 */
data class LifecycleEventCallbacks(
    var onCreate: () -> Unit = {},
    var onStart: () -> Unit = {},
    var onResume: () -> Unit = {},
    var onPause: () -> Unit = {},
    var onStop: () -> Unit = {},
    var onDestroy: () -> Unit = {}
)
