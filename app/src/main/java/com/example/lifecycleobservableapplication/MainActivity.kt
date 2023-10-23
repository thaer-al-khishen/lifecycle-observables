package com.example.lifecycleobservableapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    var firstUpdateCount = 0
    var uniqueUpdateCount = 0
    var normalUpdateCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_target_single).text = "Single Count ${viewModel.oneTimeEvent.value.toString()}"
        findViewById<TextView>(R.id.tv_target_unique).text = "Unique Count ${viewModel.uniqueUpdateEvent.value.toString()}"
        findViewById<TextView>(R.id.tv_target_normal).text = "Normal Count ${viewModel.normalUpdateEvent.value.toString()}"

        findViewById<Button>(R.id.btn_invoke_single_update_observer).setOnClickListener {
            viewModel.updateOneTimeEvent()
        }

        findViewById<Button>(R.id.btn_invoke_unique_update_observer).setOnClickListener {
            viewModel.updateUniqueEvent()
        }

        findViewById<Button>(R.id.btn_invoke_normal_update_data).setOnClickListener {
            viewModel.updateNormalEvent()
        }

        findViewById<Button>(R.id.btn_invoke_navigation).setOnClickListener {
            startActivity(Intent(this, DestinationActivity::class.java))
        }

        viewModel.oneTimeEvent.observe(lifecycle) { old, new ->
            Log.d("ThaerOutput oneTimeEvent", new.toString())
            firstUpdateCount++
            findViewById<TextView>(R.id.tv_target_single).text = "Single Count $firstUpdateCount"
        }

        viewModel.uniqueUpdateEvent.observe(lifecycle) { old, new ->
            Log.d("ThaerOutput uniqueUpdateEvent", new.toString())
            uniqueUpdateCount++
            findViewById<TextView>(R.id.tv_target_unique).text = "Unique Count $uniqueUpdateCount"
        }

        viewModel.normalUpdateEvent.observe(lifecycle) { old, new ->
            Log.d("ThaerOutput normalUpdateEvent", new.toString())
            normalUpdateCount++
            findViewById<TextView>(R.id.tv_target_normal).text = "Normal Count $normalUpdateCount"
        }

    }

}
