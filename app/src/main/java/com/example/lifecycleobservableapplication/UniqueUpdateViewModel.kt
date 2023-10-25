package com.example.lifecycleobservableapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables_core.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables_core.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables_core.UpdateCondition
import kotlinx.coroutines.launch

class UniqueUpdateViewModel : ViewModel() {

    private val _uniqueEventSurvivingConfigChange = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = true,
    )

    val uniqueEventSurvivingConfigChange: LifecycleAwareObserver<Int> = _uniqueEventSurvivingConfigChange

    private val _uniqueEventNotSurvivingConfigChange = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false,
    )

    val uniqueEventNotSurvivingConfigChange: LifecycleAwareObserver<Int> = _uniqueEventNotSurvivingConfigChange

    fun updateUniqueEventSurvivingConfigChange() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()

            val currentValue = uniqueEventSurvivingConfigChange.value ?: 0
            if (currentValue < 5) {
                _uniqueEventSurvivingConfigChange.update(uniqueEventSurvivingConfigChange.value?.plus(1), updateCondition = UpdateCondition.UNIQUE)
            } else _uniqueEventSurvivingConfigChange.update(uniqueEventSurvivingConfigChange.value?.plus(0), updateCondition = UpdateCondition.UNIQUE)

            EspressoIdlingResource.decrement()
        }
    }

    fun updateUniqueEventNotSurvivingConfigChange() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()

            val currentValue = uniqueEventNotSurvivingConfigChange.value ?: 0
            if (currentValue < 5) {
                _uniqueEventNotSurvivingConfigChange.update(uniqueEventNotSurvivingConfigChange.value?.plus(1), updateCondition = UpdateCondition.UNIQUE)
            } else _uniqueEventNotSurvivingConfigChange.update(uniqueEventNotSurvivingConfigChange.value?.plus(0), updateCondition = UpdateCondition.UNIQUE)

            EspressoIdlingResource.decrement()
        }
    }

}
