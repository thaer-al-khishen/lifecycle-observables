package com.example.lifecycleobservableapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lifecycleobservableapplication.ui.theme.LifecycleObservableApplicationTheme
import com.relatablecode.lifecycleobservables.UpdateCondition
import com.relatablecode.lifecycleobservables.asStateFlow
import com.relatablecode.lifecycleobservables.collectAsStateWithLifecycle

class ComposeActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifecycleObservableApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android", viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val viewModelValue = viewModel.normalUpdateEvent.collectAsStateWithLifecycle(updateCondition = UpdateCondition.FIRST_ONLY)

    Text(
        text = "Hello ${viewModelValue.value}!",
        modifier = modifier.clickable {
            viewModel.updateNormalEvent()
        }
    )
}
