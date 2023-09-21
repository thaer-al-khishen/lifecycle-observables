package com.relatablecode.lifecycleobservables

import androidx.lifecycle.Lifecycle

interface LifecycleAwareObservable<T> {
    var value: T?
    fun observe(lifecycle: Lifecycle, observer: (old: T?, new: T?) -> Unit)
}

/*
This naming convention clearly delineates the roles of each component in the observer pattern,
with the "observer" being a passive entity that observes changes in the "subject",
and the "observable" interface defining the contract for what an observable entity should be able to do.
*/
