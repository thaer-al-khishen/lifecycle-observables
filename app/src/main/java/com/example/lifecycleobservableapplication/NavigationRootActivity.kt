package com.example.lifecycleobservableapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.relatablecode.lifecycleobservables_broadcast.LifecycleAwareBroadcaster

class NavigationRootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_root)

        LifecycleAwareBroadcaster.initialize<AppEvent>(initialValue = AppEvent.InitialValue)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                LifecycleAwareBroadcaster.getInstance<AppEvent>()
                    .broadcastEvent(AppEvent.EventEmission("Event emitted!"))

            }, 500
        )
    }
}

sealed class AppEvent {
    data object InitialValue : AppEvent()
    data class EventEmission(val message: String) : AppEvent()
}
