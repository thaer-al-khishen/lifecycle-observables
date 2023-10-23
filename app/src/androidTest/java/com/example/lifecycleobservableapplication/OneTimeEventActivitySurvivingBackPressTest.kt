package com.example.lifecycleobservableapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OneTimeEventActivitySurvivingBackPressTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(OneTimeEventActivitySurvivingBackPress::class.java)

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
    fun test_back_press_data_survives() {
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_navigate))
            .perform(ViewActions.click())
        pressBack()
        onView(ViewMatchers.withId(R.id.tv_target_config_change_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 1"))));
    }

    @Test
    fun test_config_change_data_doesnt_survive() {
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_navigate)).perform(ViewActions.click())
        pressBack()
        onView(ViewMatchers.withId(R.id.tv_target_config_change_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 1"))));
    }

    @Test
    fun test_config_change_data_survives_multiple_emissions() {
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_navigate)).perform(ViewActions.click())
        pressBack()
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.tv_target_config_change_surviving_with_multiple_emissions))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 1"))));
    }

    @Test
    fun test_config_change_data_doesnt_survive_multiple_emissions() {
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
//        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
//            .perform(ViewActions.click())
//        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
//            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_navigate)).perform(ViewActions.click())
//        delay(200)
        pressBack()
//        delay(200)
        onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
//        delay(500)
        onView(ViewMatchers.withId(R.id.tv_target_config_change_not_surviving_with_multiple_emissions))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 1"))));
    }

}