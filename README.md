# Lifecycle Observables
A Kotlin library that provides lifecycle-aware observables for Android, allowing you to safely observe and mutate data in sync with the Android lifecycle.

## Features:
- <b>Lifecycle-Aware:</b> Observables that are aware of the Android lifecycle, ensuring safe operations that respect the lifecycle state.</br>
- <b>Concurrency Control:</b> Built-in concurrency mechanisms to ensure data integrity.</br>
- <b>Easy Integration:</b> Seamlessly integrates with Android's ViewModel and Lifecycle components.</br>

## Installation:
To use the Lifecycle Observables library in your project, add the following to your `settings.gradle` file
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        // Other repositories
    }
}
```

Then, add the dependency to your module-level build.gradle file:
```groovy
implementation "com.github.thaer-al-khishen:lifecycle-observables:1.2.0-beta07"
```
## Usage:

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

## License:
This project is licensed under the Apache 2.0 License. Check the LICENSE file for details.
