package com.example.lifecycleobservableapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UniqueUpdateActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(UniqueUpdateActivity::class.java)

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
    fun test_config_change_data_survives() {
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        onView(ViewMatchers.withId(R.id.tv_target_unique_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 3"))))
    }

    @Test
    fun test_config_change_data_survives_doesnt_exceed_5() {
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.tv_target_unique_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 5"))))
    }

    @Test
    fun test_config_change_data_doesnt_survive() {
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        onView(ViewMatchers.withId(R.id.tv_target_unique_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 0"))))
    }

    @Test
    fun test_config_change_data_doesnt_survive_doesnt_exceed_5() {
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.tv_target_unique_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 5"))))
    }

}
