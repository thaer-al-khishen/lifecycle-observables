package com.example.lifecycleobservableapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables.UpdateMode

class MainViewModel: ViewModel() {

    private var count = 0

    private val _selectedCurrency = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false
    )

    val selectedCurrency: LifecycleAwareObserver<Int> = _selectedCurrency

    fun updateValue() {
        count++
        _selectedCurrency.update(count, updateMode = UpdateMode.ASYNC)
    }

}
