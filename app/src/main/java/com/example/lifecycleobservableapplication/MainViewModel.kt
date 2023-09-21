package com.example.lifecycleobservableapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables.LifecycleAwareSubject

class MainViewModel: ViewModel() {

    private val _selectedCurrency = LifecycleAwareSubject(
        initialValue = { "Old value" },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false
    )

    val selectedCurrency: LifecycleAwareObserver<String> = _selectedCurrency

    fun updateValue() {
        _selectedCurrency.asyncUpdate("New value")
    }

}
