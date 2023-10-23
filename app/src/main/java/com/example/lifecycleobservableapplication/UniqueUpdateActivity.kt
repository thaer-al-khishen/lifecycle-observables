package com.example.lifecycleobservableapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class UniqueUpdateActivity : AppCompatActivity() {

    private val viewModel: UniqueUpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_update)

        var uniqueUpdateCountSurvivingConfigChange = viewModel.uniqueEventSurvivingConfigChange.value ?: 0
        var uniqueUpdateCountNotSurvivingConfigChange = viewModel.uniqueEventNotSurvivingConfigChange.value ?: 0

        findViewById<android.widget.TextView>(R.id.tv_target_unique_surviving).text = "Unique Update Count ${uniqueUpdateCountSurvivingConfigChange}"
        findViewById<android.widget.TextView>(R.id.tv_target_unique_not_surviving).text = "Unique Update Count ${uniqueUpdateCountNotSurvivingConfigChange}"

        findViewById<android.widget.Button>(R.id.btn_invoke_unique_observer_surviving).setOnClickListener {
            viewModel.updateUniqueEventSurvivingConfigChange()
        }
        findViewById<android.widget.Button>(R.id.btn_invoke_unique_observer_not_surviving).setOnClickListener {
            viewModel.updateUniqueEventNotSurvivingConfigChange()
        }

        viewModel.uniqueEventSurvivingConfigChange.observe(lifecycle) { old, new ->
            uniqueUpdateCountSurvivingConfigChange++
            findViewById<android.widget.TextView>(R.id.tv_target_unique_surviving).text = "Unique Update Count ${uniqueUpdateCountSurvivingConfigChange}"
        }

        viewModel.uniqueEventNotSurvivingConfigChange.observe(lifecycle) { old, new ->
            uniqueUpdateCountNotSurvivingConfigChange++
            findViewById<android.widget.TextView>(R.id.tv_target_unique_not_surviving).text = "Unique Update Count ${uniqueUpdateCountNotSurvivingConfigChange}"
        }

    }

}
