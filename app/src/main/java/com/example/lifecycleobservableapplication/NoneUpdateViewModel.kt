package com.example.lifecycleobservableapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables_core.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables_core.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables_core.UpdateCondition
import kotlinx.coroutines.launch

class NoneUpdateViewModel : ViewModel() {

    private val _noneEventSurvivingConfigChange = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = true,
    )

    val noneEventSurvivingConfigChange: LifecycleAwareObserver<Int> = _noneEventSurvivingConfigChange

    private val _noneEventNotSurvivingConfigChange = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false,
    )

    val noneEventNotSurvivingConfigChange: LifecycleAwareObserver<Int> = _noneEventNotSurvivingConfigChange

    fun updateNoneEventSurvivingConfigChange() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()

            val currentValue = noneEventSurvivingConfigChange.value ?: 0
            if (currentValue < 5) {
                _noneEventSurvivingConfigChange.update(noneEventSurvivingConfigChange.value?.plus(1), updateCondition = UpdateCondition.NONE)
            } else _noneEventSurvivingConfigChange.update(noneEventSurvivingConfigChange.value?.plus(0), updateCondition = UpdateCondition.NONE)

            EspressoIdlingResource.decrement()
        }
    }

    fun updateNoneEventNotSurvivingConfigChange() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()

            val currentValue = noneEventNotSurvivingConfigChange.value ?: 0
            if (currentValue < 5) {
                _noneEventNotSurvivingConfigChange.update(noneEventNotSurvivingConfigChange.value?.plus(1), updateCondition = UpdateCondition.NONE)
            } else _noneEventNotSurvivingConfigChange.update(noneEventNotSurvivingConfigChange.value?.plus(0), updateCondition = UpdateCondition.NONE)

            EspressoIdlingResource.decrement()
        }
    }
}
