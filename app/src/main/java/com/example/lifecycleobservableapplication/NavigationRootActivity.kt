package com.example.lifecycleobservableapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NavigationRootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_root)
    }
}