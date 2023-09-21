# Lifecycle Observables
A Kotlin library that provides lifecycle-aware observables for Android, allowing you to safely observe and mutate data in sync with the Android lifecycle.

Features:</br>
<b>Lifecycle-Aware:</b> Observables that are aware of the Android lifecycle, ensuring safe operations that respect the lifecycle state.</br>
<b>Concurrency Control:</b> Built-in concurrency mechanisms to ensure data integrity.</br>
<b>Easy Integration:</b> Seamlessly integrates with Android's ViewModel and Lifecycle components.</br>

<b>Installation:</b></br>

Add the following dependency to your build.gradle:</br>
```groovy
implementation 'com.github.thaer-al-khishen:lifecycle-observables:1.1.0'
```
Usage:

```kotlin
class MainViewModel: ViewModel() {

    private val _selectedCurrency = LifecycleAwareSubject(
        initialValue = { "Old value" },
        coroutineScope = viewModelScope,
        shouldSurviveConfigurationChange = false
    )

    val selectedCurrency: LifecycleAwareObserver<String> = _selectedCurrency

    fun updateValue() {
        _selectedCurrency.update("New value", updateMode = UpdateMode.ASYNC)
    }

}
```

Observe and interact with the observables in your Activity or Fragment:
```kotlin
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_invoke).setOnClickListener {
            viewModel.updateValue()
        }

        viewModel.selectedCurrency.observe(lifecycle) { old, new ->
            Log.d("Output", old.toString())
            Log.d("Output", new.toString())
        }
    }
}
```

License
This project is licensed under the Apache 2.0 License. Check the LICENSE file for details.
