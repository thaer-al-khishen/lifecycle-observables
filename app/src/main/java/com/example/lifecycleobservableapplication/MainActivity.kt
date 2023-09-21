package com.example.lifecycleobservableapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_invoke).setOnClickListener {
            viewModel.updateValue()
        }

        viewModel.selectedCurrency.observe(lifecycle) {old, new ->
            Log.d("ThaerOutput", old.toString())
            Log.d("ThaerOutput", new.toString())
        }

    }

}
