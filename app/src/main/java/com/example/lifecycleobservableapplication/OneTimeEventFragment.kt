package com.example.lifecycleobservableapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels

class OneTimeEventFragment : Fragment() {

    private val viewModel: OneTimeEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one_time_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_surviving).text = "Config Change Count ${viewModel.oneTimeEventSurvivingConfigChange.value.toString()}"

        view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_not_surviving).text = "Config Change Count ${viewModel.oneTimeEventNotSurvivingConfigChange.value.toString()}"

        view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_surviving_with_multiple_emissions).text = "Config Change Count ${viewModel.oneTimeEventSurvivingConfigChangeMultipleEmissions.value.toString()}"

        view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_not_surviving_with_multiple_emissions).text = "Config Change Count ${viewModel.oneTimeEventNotSurvivingConfigChangeMultipleEmissions.value.toString()}"

        view.findViewById<android.widget.Button>(R.id.btn_invoke_config_change_observer_surviving).setOnClickListener {
            viewModel.updateOneTimeEventSurvivingConfigChange()
        }

        view.findViewById<android.widget.Button>(R.id.btn_invoke_config_change_observer_not_surviving).setOnClickListener {
            viewModel.updateOneTimeEventNotSurvivingConfigChange()
        }

        view.findViewById<android.widget.Button>(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions).setOnClickListener {
            viewModel.updateOneTimeEventSurvivingConfigChangeWithMultipleEmissions()
        }

        view.findViewById<android.widget.Button>(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions).setOnClickListener {
            viewModel.updateOneTimeEventNotSurvivingConfigChangeWithMultipleEmissions()
        }

        viewModel.oneTimeEventSurvivingConfigChange.observe(lifecycle) { old, new ->
            android.util.Log.d("ThaerOutput oneTimeEvent", new.toString())
            view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_surviving).text = "Config Change Count ${new.toString()}"
        }

        viewModel.oneTimeEventNotSurvivingConfigChange.observe(lifecycle) { old, new ->
            view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_not_surviving).text = "Config Change Count ${new.toString()}"
        }

        viewModel.oneTimeEventSurvivingConfigChangeMultipleEmissions.observe(lifecycle) { old, new ->
            view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_surviving_with_multiple_emissions).text = "Config Change Count ${new.toString()}"
        }

        viewModel.oneTimeEventNotSurvivingConfigChangeMultipleEmissions.observe(lifecycle) { old, new ->
            view.findViewById<android.widget.TextView>(R.id.tv_target_config_change_not_surviving_with_multiple_emissions).text = "Config Change Count ${new.toString()}"
        }

    }

}
