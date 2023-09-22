package com.example.lifecycleobservableapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.relatablecode.lifecycleobservables.UpdateCondition
import com.relatablecode.lifecycleobservables.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_invoke).setOnClickListener {
            viewModel.updateValue()
        }

        lifecycleScope.launch {
            viewModel.selectedCurrency.asStateFlow(lifecycle, updateCondition = UpdateCondition.FIRST_ONLY).collect {
                Log.d("ThaerOutput", it.toString())
//                Log.d("ThaerOutput", new.toString())
            }
        }


    }

}
