package com.example.lifecycleobservableapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels

class NoneUpdateFragment : Fragment() {

    private val viewModel: NoneUpdateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_none_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var noneUpdateCountSurvivingConfigChange = viewModel.noneEventSurvivingConfigChange.value ?: 0
        var noneUpdateCountNotSurvivingConfigChange = viewModel.noneEventNotSurvivingConfigChange.value ?: 0

        view.findViewById<android.widget.TextView>(R.id.tv_target_none_surviving).text = "None Update Count ${noneUpdateCountSurvivingConfigChange}"
        view.findViewById<android.widget.TextView>(R.id.tv_target_none_not_surviving).text = "None Update Count ${noneUpdateCountNotSurvivingConfigChange}"

        view.findViewById<android.widget.Button>(R.id.btn_invoke_none_observer_surviving).setOnClickListener {
            viewModel.updateNoneEventSurvivingConfigChange()
        }
        view.findViewById<android.widget.Button>(R.id.btn_invoke_none_observer_not_surviving).setOnClickListener {
            viewModel.updateNoneEventNotSurvivingConfigChange()
        }

        viewModel.noneEventSurvivingConfigChange.observe(lifecycle) { old, new ->
            noneUpdateCountSurvivingConfigChange++
            view.findViewById<android.widget.TextView>(R.id.tv_target_none_surviving).text = "None Update Count ${noneUpdateCountSurvivingConfigChange}"
        }

        viewModel.noneEventNotSurvivingConfigChange.observe(lifecycle) { old, new ->
            noneUpdateCountNotSurvivingConfigChange++
            view.findViewById<android.widget.TextView>(R.id.tv_target_none_not_surviving).text = "None Update Count ${noneUpdateCountNotSurvivingConfigChange}"
        }

    }

}
