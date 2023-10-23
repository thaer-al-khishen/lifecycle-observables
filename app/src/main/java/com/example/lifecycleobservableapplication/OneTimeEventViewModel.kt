package com.example.lifecycleobservableapplication

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables.UpdateCondition
import kotlinx.coroutines.launch

class OneTimeEventViewModel : ViewModel() {

    private val _oneTimeEventSurvivingConfigChange = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = true,
        shouldResetFirstEmission = false
    )

    val oneTimeEventSurvivingConfigChange: LifecycleAwareObserver<Int> =
        _oneTimeEventSurvivingConfigChange

    private val _oneTimeEventNotSurvivingConfigChange = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false,
        shouldResetFirstEmission = false
    )

    val oneTimeEventNotSurvivingConfigChange: LifecycleAwareObserver<Int> =
        _oneTimeEventNotSurvivingConfigChange

    private val _oneTimeEventSurvivingConfigChangeMultipleEmissions = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = true,
        shouldResetFirstEmission = true
    )

    val oneTimeEventSurvivingConfigChangeMultipleEmissions: LifecycleAwareObserver<Int> =
        _oneTimeEventSurvivingConfigChangeMultipleEmissions

    private val _oneTimeEventNotSurvivingConfigChangeMultipleEmissions = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false,
        shouldResetFirstEmission = true
    )

    val oneTimeEventNotSurvivingConfigChangeMultipleEmissions: LifecycleAwareObserver<Int> =
        _oneTimeEventNotSurvivingConfigChangeMultipleEmissions

    fun updateOneTimeEventSurvivingConfigChange() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            kotlinx.coroutines.delay(1000)
            _oneTimeEventSurvivingConfigChange.update(
                oneTimeEventSurvivingConfigChange.value?.plus(
                    1
                ),
                updateCondition = UpdateCondition.FIRST_ONLY
            )
            EspressoIdlingResource.decrement()
        }
    }

    fun updateOneTimeEventNotSurvivingConfigChange() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            _oneTimeEventNotSurvivingConfigChange.update(
                oneTimeEventNotSurvivingConfigChange.value?.plus(
                    1
                ),
                updateCondition = UpdateCondition.FIRST_ONLY
            )
            EspressoIdlingResource.decrement()
        }
    }

    fun updateOneTimeEventSurvivingConfigChangeWithMultipleEmissions() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            _oneTimeEventSurvivingConfigChangeMultipleEmissions.update(
                oneTimeEventSurvivingConfigChangeMultipleEmissions.value?.plus(1),
                updateCondition = UpdateCondition.FIRST_ONLY
            )
            EspressoIdlingResource.decrement()
        }
    }

    fun updateOneTimeEventNotSurvivingConfigChangeWithMultipleEmissions() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            _oneTimeEventNotSurvivingConfigChangeMultipleEmissions.update(
                oneTimeEventNotSurvivingConfigChangeMultipleEmissions.value?.plus(1),
                updateCondition = UpdateCondition.FIRST_ONLY
            )
            EspressoIdlingResource.decrement()
        }
    }

}
