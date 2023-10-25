package com.example.lifecycleobservableapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.relatablecode.lifecycleobservables_extensions.asLiveData
import kotlinx.coroutines.launch

class OneTimeEventActivitySurvivingBackPress : AppCompatActivity() {

    private val viewModel: OneTimeEventViewModelSurvivingBackPress by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_time_event_surviving_back_press)

        findViewById<TextView>(R.id.tv_target_config_change_surviving).text =
            "Config Change Count ${viewModel.oneTimeEventSurvivingConfigChange.value.toString()}"

        findViewById<TextView>(R.id.tv_target_config_change_not_surviving).text =
            "Config Change Count ${viewModel.oneTimeEventNotSurvivingConfigChange.value.toString()}"

        findViewById<TextView>(R.id.tv_target_config_change_surviving_with_multiple_emissions).text =
            "Config Change Count ${viewModel.oneTimeEventSurvivingConfigChangeMultipleEmissions.value.toString()}"

        findViewById<TextView>(R.id.tv_target_config_change_not_surviving_with_multiple_emissions).text =
            "Config Change Count ${viewModel.oneTimeEventNotSurvivingConfigChangeMultipleEmissions.value.toString()}"

        findViewById<Button>(R.id.btn_invoke_config_change_observer_surviving).setOnClickListener {
            viewModel.updateOneTimeEventSurvivingConfigChange()
        }

        findViewById<Button>(R.id.btn_invoke_config_change_observer_not_surviving).setOnClickListener {
            viewModel.updateOneTimeEventNotSurvivingConfigChange()
        }

        findViewById<Button>(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions).setOnClickListener {
            viewModel.updateOneTimeEventSurvivingConfigChangeWithMultipleEmissions()
        }

        findViewById<Button>(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions).setOnClickListener {
            viewModel.updateOneTimeEventNotSurvivingConfigChangeWithMultipleEmissions()
        }

        findViewById<Button>(R.id.btn_navigate).setOnClickListener {
            startActivity(Intent(this, DestinationActivity::class.java))
        }

        lifecycleScope.launch {
            viewModel.oneTimeEventSurvivingConfigChange.asLiveData().observe(this@OneTimeEventActivitySurvivingBackPress) {
//                android.util.Log.d("ThaerOutput oneTimeEvent", new.toString())
                findViewById<TextView>(R.id.tv_target_config_change_surviving).text =
                    "Config Change Count ${it.toString()}"
            }
        }

        lifecycleScope.launch {

            viewModel.oneTimeEventNotSurvivingConfigChange.asLiveData().observe(this@OneTimeEventActivitySurvivingBackPress) {
                findViewById<TextView>(R.id.tv_target_config_change_not_surviving).text =
                    "Config Change Count ${it.toString()}"
            }
        }

        lifecycleScope.launch {
            viewModel.oneTimeEventSurvivingConfigChangeMultipleEmissions.asLiveData().observe(this@OneTimeEventActivitySurvivingBackPress) {
                findViewById<TextView>(R.id.tv_target_config_change_surviving_with_multiple_emissions).text =
                    "Config Change Count ${it.toString()}"
            }
        }

        viewModel.oneTimeEventNotSurvivingConfigChangeMultipleEmissions.observe(lifecycle) { old, new ->
            findViewById<TextView>(R.id.tv_target_config_change_not_surviving_with_multiple_emissions).text =
                "Config Change Count ${new.toString()}"
        }

    }

}
