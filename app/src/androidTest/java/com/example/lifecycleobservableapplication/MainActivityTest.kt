package com.example.lifecycleobservableapplication

import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.lifecycleobservableapplication.OrientationChangeAction.Companion.orientationLandscape
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    //     Register your Idling Resource before any tests regarding this component
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    // Unregister your Idling Resource so it can be garbage collected and does not leak any memory
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun test_click_button_invoke_once_observe_new_value() {

        val latch = CountDownLatch(1)
        var viewModel: MainViewModel? = null
        var countToTrack = 0

        activityRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
            activity.lifecycleScope.launch {
                viewModel?.normalUpdateEvent?.observe(activity.lifecycle) { old, new ->
                    countToTrack = new ?: 0
                    latch.countDown() // Decrement the count of the latch
                }
            }
        }

        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())

        latch.await(5, TimeUnit.SECONDS) // Wait until the latch count reaches zero

        assertTrue(countToTrack == 1)

    }

    @Test
    fun test_single_update_observer() {
        onView(withId(R.id.btn_invoke_single_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_single_update_observer)).perform(click())
        onView(withId(R.id.tv_target_single)).check(matches(withText(containsString("Single Count 1"))));
    }

    @Test
    fun test_unique_update_observer() {
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.btn_invoke_unique_update_observer)).perform(click())
        onView(withId(R.id.tv_target_unique)).check(matches(withText(containsString("Unique Count 5"))));
    }

    @Test
    fun test_normal_update_observer() {
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.btn_invoke_normal_update_data)).perform(click())
        onView(withId(R.id.tv_target_normal)).check(matches(withText(containsString("Normal Count 7"))));
    }

}
