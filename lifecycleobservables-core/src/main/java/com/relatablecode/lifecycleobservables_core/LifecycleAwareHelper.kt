package com.relatablecode.lifecycleobservables_core

import androidx.lifecycle.Lifecycle

/**
 * A helper object that provides default configuration values for the `LifecycleAwareSubject`.
 *
 * This allows users to set global default behaviors for all instances of `LifecycleAwareSubject`
 * across the application. It's recommended to set these default values during the application's
 * initialization phase to avoid unexpected behaviors.
 *
 * Note: Ensure that modifications to these defaults are done in a thread-safe manner if they
 * are accessed from multiple threads.
 */
object LifecycleAwareHelper {

    /**
     * Specifies the default lifecycle event at which the `LifecycleAwareSubject` is instantiated.
     *
     * By default, it is set to `Lifecycle.Event.ON_START`.
     */
    var defaultInstantiatedAt = Lifecycle.Event.ON_START

    /**
     * Specifies the default lifecycle event at which the `LifecycleAwareSubject` is destroyed.
     *
     * By default, it is set to `Lifecycle.Event.ON_STOP`.
     */
    var defaultDestroyedAt = Lifecycle.Event.ON_STOP

    /**
     * Indicates whether the `LifecycleAwareSubject` should survive configuration changes, like screen rotations.
     *
     * By default, this is set to `true`.
     */
    var defaultShouldSurviveConfigurationChange = true

    /**
     * Indicates whether the first emission of the `LifecycleAwareSubject` should be reset.
     *
     * By default, this is set to `true`.
     */
    var defaultShouldResetFirstEmission = true
}
