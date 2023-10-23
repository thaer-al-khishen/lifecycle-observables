package com.example.lifecycleobservableapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables.UpdateCondition
import com.relatablecode.lifecycleobservables.UpdateMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _oneTimeEvent = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope
    )

    val oneTimeEvent: LifecycleAwareObserver<Int> = _oneTimeEvent

    private val _uniqueUpdateEvent = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope
    )
    val uniqueUpdateEvent: LifecycleAwareObserver<Int> = _uniqueUpdateEvent


    private val _normalUpdateEvent = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope
    )
    val normalUpdateEvent: LifecycleAwareObserver<Int> = _normalUpdateEvent

    fun updateOneTimeEvent() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            delay(1000)
            _oneTimeEvent.update(oneTimeEvent.value?.plus(1), updateCondition = UpdateCondition.FIRST_ONLY)
            EspressoIdlingResource.decrement()
        }
    }

    fun updateUniqueEvent() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            val currentValue = uniqueUpdateEvent.value ?: 0
            if (currentValue < 5) {
                _uniqueUpdateEvent.update(uniqueUpdateEvent.value?.plus(1), updateCondition = UpdateCondition.UNIQUE)
            }
            EspressoIdlingResource.decrement()
        }
    }

    fun updateNormalEvent() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            val currentValue = normalUpdateEvent.value ?: 0
            if (currentValue < 5) {
                _normalUpdateEvent.update(normalUpdateEvent.value?.plus(1), updateCondition = UpdateCondition.NONE)
            } else _normalUpdateEvent.update(normalUpdateEvent.value?.plus(0), updateCondition = UpdateCondition.NONE)
            EspressoIdlingResource.decrement()
        }

    }

}
