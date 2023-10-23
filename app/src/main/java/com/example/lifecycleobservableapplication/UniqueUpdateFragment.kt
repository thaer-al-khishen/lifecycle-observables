package com.example.lifecycleobservableapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels

class UniqueUpdateFragment : Fragment() {

    private val viewModel: UniqueUpdateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unique_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var uniqueUpdateCountSurvivingConfigChange = viewModel.uniqueEventSurvivingConfigChange.value ?: 0
        var uniqueUpdateCountNotSurvivingConfigChange = viewModel.uniqueEventNotSurvivingConfigChange.value ?: 0

        view.findViewById<android.widget.TextView>(R.id.tv_target_unique_surviving).text = "Unique Update Count ${uniqueUpdateCountSurvivingConfigChange}"
        view.findViewById<android.widget.TextView>(R.id.tv_target_unique_not_surviving).text = "Unique Update Count ${uniqueUpdateCountNotSurvivingConfigChange}"

        view.findViewById<android.widget.Button>(R.id.btn_invoke_unique_observer_surviving).setOnClickListener {
            viewModel.updateUniqueEventSurvivingConfigChange()
        }
        view.findViewById<android.widget.Button>(R.id.btn_invoke_unique_observer_not_surviving).setOnClickListener {
            viewModel.updateUniqueEventNotSurvivingConfigChange()
        }

        viewModel.uniqueEventSurvivingConfigChange.observe(lifecycle) { old, new ->
            uniqueUpdateCountSurvivingConfigChange++
            view.findViewById<android.widget.TextView>(R.id.tv_target_unique_surviving).text = "Unique Update Count ${uniqueUpdateCountSurvivingConfigChange}"
        }

        viewModel.uniqueEventNotSurvivingConfigChange.observe(lifecycle) { old, new ->
            uniqueUpdateCountNotSurvivingConfigChange++
            view.findViewById<android.widget.TextView>(R.id.tv_target_unique_not_surviving).text = "Unique Update Count ${uniqueUpdateCountNotSurvivingConfigChange}"
        }

    }

}
