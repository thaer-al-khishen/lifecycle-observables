package com.relatablecode.lifecycleobservables

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope

context(LifecycleOwner)
fun <T, R> LifecycleAwareObserver<T>.map(transform: (T?) -> R): LifecycleAwareObserver<R> {
    val newSubject = LifecycleAwareSubject<R>(this@LifecycleOwner.lifecycle.coroutineScope)
    this.observe(this@LifecycleOwner.lifecycle) { old, new ->
        newSubject.update(transform(new))
    }
    return newSubject
}

context(LifecycleOwner)
fun <T, R> LifecycleAwareObserver<T>.flatMap(transform: (T?) -> LifecycleAwareObserver<R>): LifecycleAwareObserver<R> {
    val newSubject = LifecycleAwareSubject<R>(this@LifecycleOwner.lifecycle.coroutineScope)
    this.observe(this@LifecycleOwner.lifecycle) { old, new ->
        val transformedObserver = transform(new)
        transformedObserver.observe(this@LifecycleOwner.lifecycle) { _, transformedNew ->
            newSubject.update(transformedNew)
        }
    }
    return newSubject
}

context(LifecycleOwner)
fun <T> LifecycleAwareObserver<T>.filter(predicate: (T?) -> Boolean): LifecycleAwareObserver<T> {
    val newSubject = LifecycleAwareSubject<T>(this@LifecycleOwner.lifecycle.coroutineScope)
    this.observe(this@LifecycleOwner.lifecycle) { old, new ->
        if (predicate(new)) {
            newSubject.update(new)
        }
    }
    return newSubject
}

context(LifecycleOwner)
fun <T> LifecycleAwareObserver<T>.distinctUntilChanged(): LifecycleAwareObserver<T> {
    val newSubject = LifecycleAwareSubject<T>(this@LifecycleOwner.lifecycle.coroutineScope).apply {
        initialValue = this@distinctUntilChanged.initialValue
        value = initialValue?.invoke()
    }
    var lastValue: T? = initialValue?.invoke()
    this.observe(this@LifecycleOwner.lifecycle) { old, new ->
        if (new != lastValue) {
            lastValue = new
            newSubject.update(new)
        }
    }
    return newSubject
}

context(LifecycleOwner)
fun <T1, T2, R> combineLatest(
    source1: LifecycleAwareObserver<T1>,
    source2: LifecycleAwareObserver<T2>,
    combineFunction: (T1, T2) -> R
): LifecycleAwareObserver<R> {
    val newSubject = LifecycleAwareSubject<R>(this@LifecycleOwner.lifecycle.coroutineScope)
    var lastValue1: T1? = null
    var lastValue2: T2? = null
    source1.observe(this@LifecycleOwner.lifecycle) { _, new1 ->
        lastValue1 = new1
        if (lastValue1 != null && lastValue2 != null) {
            newSubject.update(combineFunction(lastValue1!!, lastValue2!!))
        }
    }
    source2.observe(this@LifecycleOwner.lifecycle) { _, new2 ->
        lastValue2 = new2
        if (lastValue1 != null && lastValue2 != null) {
            newSubject.update(combineFunction(lastValue1!!, lastValue2!!))
        }
    }
    return newSubject
}
