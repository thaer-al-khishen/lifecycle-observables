package com.example.lifecycleobservableapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.relatablecode.lifecycleobservables.UpdateCondition
import com.relatablecode.lifecycleobservables.asSharedFlow
import com.relatablecode.lifecycleobservables.asStateFlow
import com.relatablecode.lifecycleobservables.combineLatest
import com.relatablecode.lifecycleobservables.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_invoke).setOnClickListener {
            viewModel.updateValue()
        }

        findViewById<Button>(R.id.btn_invoke_navigation).setOnClickListener {
            startActivity(Intent(this, DestinationActivity::class.java))
        }

        lifecycleScope.launch {
            viewModel.selectedCurrency.asStateFlow(updateCondition = UpdateCondition.FIRST_ONLY)
                .collect {
                    Log.d("ThaerOutput SharedFlow", it.toString())
                }
        }

        viewModel.themeObserver.distinctUntilChanged().observe(lifecycle) { old, new ->
            Log.d("ThemeChange", "Theme changed from $old to $new")
        }

        viewModel.themeObserver.update("Lights")
        viewModel.themeObserver.update("Dark")
        viewModel.themeObserver.update("Dark")

        combineLatest(viewModel.currencyObserver, viewModel.languageObserver) { currency, language ->
            "Selected currency is $currency and language is $language"
        }.observe(lifecycle) { _, combinedMessage ->
            Log.d("CombinedSelection", combinedMessage.toString())
        }

        viewModel.currencyObserver.update("EUR")
        viewModel.languageObserver.update("French")

    }

}
