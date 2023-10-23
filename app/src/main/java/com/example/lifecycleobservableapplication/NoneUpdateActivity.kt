package com.example.lifecycleobservableapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class NoneUpdateActivity : AppCompatActivity() {

    private val viewModel: NoneUpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_none_update)

        var noneUpdateCountSurvivingConfigChange = viewModel.noneEventSurvivingConfigChange.value ?: 0
        var noneUpdateCountNotSurvivingConfigChange = viewModel.noneEventNotSurvivingConfigChange.value ?: 0

        findViewById<android.widget.TextView>(R.id.tv_target_none_surviving).text = "None Update Count ${noneUpdateCountSurvivingConfigChange}"
        findViewById<android.widget.TextView>(R.id.tv_target_none_not_surviving).text = "None Update Count ${noneUpdateCountNotSurvivingConfigChange}"

        findViewById<android.widget.Button>(R.id.btn_invoke_none_observer_surviving).setOnClickListener {
            viewModel.updateNoneEventSurvivingConfigChange()
        }
        findViewById<android.widget.Button>(R.id.btn_invoke_none_observer_not_surviving).setOnClickListener {
            viewModel.updateNoneEventNotSurvivingConfigChange()
        }

        viewModel.noneEventSurvivingConfigChange.observe(lifecycle) { old, new ->
            noneUpdateCountSurvivingConfigChange++
            findViewById<android.widget.TextView>(R.id.tv_target_none_surviving).text = "None Update Count ${noneUpdateCountSurvivingConfigChange}"
        }

        viewModel.noneEventNotSurvivingConfigChange.observe(lifecycle) { old, new ->
            noneUpdateCountNotSurvivingConfigChange++
            findViewById<android.widget.TextView>(R.id.tv_target_none_not_surviving).text = "None Update Count ${noneUpdateCountNotSurvivingConfigChange}"
        }

    }

}
