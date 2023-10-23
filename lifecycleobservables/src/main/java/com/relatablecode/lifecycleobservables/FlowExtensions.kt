package com.relatablecode.lifecycleobservables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> LifecycleAwareObserver<T>.asStateFlow(
    lifecycle: Lifecycle,
    updateCondition: UpdateCondition = UpdateCondition.NONE
): StateFlow<T?> {
    val stateFlow = MutableStateFlow(value)
    observe(lifecycle) { oldValue, newValue ->
        when (updateCondition) {
            UpdateCondition.FIRST_ONLY -> {
                if (oldValue == initialValue?.invoke()) {
                    stateFlow.value = newValue
                }
            }
            UpdateCondition.FIRST_ONLY_AND_NOT_NULL -> {
                if (oldValue == initialValue?.invoke() && newValue != null) {
                    stateFlow.value = newValue
                }
            }
            UpdateCondition.UNIQUE -> {
                if (oldValue != newValue) {
                    stateFlow.value = newValue
                }
            }
            UpdateCondition.NONE -> {
                stateFlow.value = newValue
            }
        }
    }
    return stateFlow
}

context(LifecycleOwner)
fun <T> LifecycleAwareObserver<T>.asStateFlow(
    updateCondition: UpdateCondition = UpdateCondition.NONE
): StateFlow<T?> {
    val stateFlow = MutableStateFlow(value)
    observe(this@LifecycleOwner.lifecycle) { oldValue, newValue ->
        when (updateCondition) {
            UpdateCondition.FIRST_ONLY -> {
                if (oldValue == initialValue?.invoke()) {
                    stateFlow.value = newValue
                }
            }
            UpdateCondition.FIRST_ONLY_AND_NOT_NULL -> {
                if (oldValue == initialValue?.invoke() && newValue != null) {
                    stateFlow.value = newValue
                }
            }
            UpdateCondition.UNIQUE -> {
                if (oldValue != newValue) {
                    stateFlow.value = newValue
                }
            }
            UpdateCondition.NONE -> {
                stateFlow.value = newValue
            }
        }
    }
    return stateFlow
}

context(LifecycleOwner)
fun <T> LifecycleAwareObserver<T>.asSharedFlow(
    updateCondition: UpdateCondition = UpdateCondition.NONE
): SharedFlow<T?> {
    val sharedFlow = MutableSharedFlow<T?>()
    observe(this@LifecycleOwner.lifecycle) { oldValue, newValue ->
        when (updateCondition) {
            UpdateCondition.FIRST_ONLY -> {
                if (oldValue == initialValue?.invoke()) {
                    this@LifecycleOwner.lifecycle.coroutineScope.launch {
                        sharedFlow.emit(newValue)
                    }
                }
            }
            UpdateCondition.FIRST_ONLY_AND_NOT_NULL -> {
                if (oldValue == initialValue?.invoke() && newValue != null) {
                    this@LifecycleOwner.lifecycle.coroutineScope.launch {
                        sharedFlow.emit(newValue)
                    }
                }
            }
            UpdateCondition.UNIQUE -> {
                if (oldValue != newValue) {
                    this@LifecycleOwner.lifecycle.coroutineScope.launch {
                        sharedFlow.emit(newValue)
                    }
                }
            }

            UpdateCondition.NONE -> {
                this@LifecycleOwner.lifecycle.coroutineScope.launch {
                    sharedFlow.emit(newValue)
                }
            }
        }
    }
    return sharedFlow
}

context(LifecycleOwner)
fun <T> LifecycleAwareObserver<T>.asLiveData(
    updateCondition: UpdateCondition = UpdateCondition.NONE
): LiveData<T?> {
    val liveData = MutableLiveData<T?>()
    observe(this@LifecycleOwner.lifecycle) { oldValue, newValue ->
        when (updateCondition) {
            UpdateCondition.FIRST_ONLY -> {
                if (oldValue == initialValue?.invoke()) {
                    liveData.value = newValue
                }
            }
            UpdateCondition.FIRST_ONLY_AND_NOT_NULL -> {
                if (oldValue == initialValue?.invoke() && newValue != null) {
                    liveData.value = newValue
                }
            }
            UpdateCondition.UNIQUE -> {
                if (oldValue != newValue) {
                    liveData.value = newValue
                }
            }

            UpdateCondition.NONE -> {
                liveData.value = newValue
            }
        }
    }
    return liveData
}

@Composable
fun <T> LifecycleAwareObserver<T>.collectAsState(updateCondition: UpdateCondition = UpdateCondition.NONE): State<T?> {
    return this.asStateFlow(LocalLifecycleOwner.current.lifecycle, updateCondition).collectAsState()
}

@Composable
fun <T> LifecycleAwareObserver<T>.collectAsStateWithLifecycle(
    updateCondition: UpdateCondition = UpdateCondition.NONE,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T?> {
    return this.asStateFlow(LocalLifecycleOwner.current.lifecycle, updateCondition)
        .collectAsStateWithLifecycle(minActiveState = minActiveState)
}
