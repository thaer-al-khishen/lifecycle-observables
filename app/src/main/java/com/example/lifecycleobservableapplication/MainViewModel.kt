package com.example.lifecycleobservableapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relatablecode.lifecycleobservables.LifecycleAwareObserver
import com.relatablecode.lifecycleobservables.LifecycleAwareSubject
import com.relatablecode.lifecycleobservables.UpdateCondition
import com.relatablecode.lifecycleobservables.UpdateMode

class MainViewModel: ViewModel() {

    private var count = 0

    private val _selectedCurrency = LifecycleAwareSubject(
        initialValue = { 0 },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false,
        shouldResetFirstEmission = false
    )

    val selectedCurrency: LifecycleAwareObserver<Int> = _selectedCurrency

    private val _currencyName = LifecycleAwareSubject<String>(
        initialValue = { "Dollar" },
        coroutineScope = viewModelScope
    )
    val currencyName: LifecycleAwareObserver<String> = _currencyName

    val themeObserver = LifecycleAwareSubject<String>(coroutineScope = viewModelScope, initialValue = { "Light" })

    val currencyObserver = LifecycleAwareSubject<String>(coroutineScope = viewModelScope, initialValue = { "USD" })
    val languageObserver = LifecycleAwareSubject<String>(coroutineScope = viewModelScope, initialValue = { "English" })

    fun updateValue() {
        if (count < 5) {
            count++
        }
        _selectedCurrency.update(count, updateMode = UpdateMode.ASYNC)
    }

    fun updateCurrencyName() {
        _currencyName.update("Euro", UpdateMode.ASYNC)
    }

}
