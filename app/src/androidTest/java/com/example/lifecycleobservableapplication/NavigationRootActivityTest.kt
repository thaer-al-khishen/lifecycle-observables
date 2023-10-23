package com.example.lifecycleobservableapplication

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
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

class NavigationRootActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(NavigationRootActivity::class.java)

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
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_config_change_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 1"))));
    }

    @Test
    fun test_config_change_data_doesnt_survive() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_config_change_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count null"))));
    }

    @Test
    fun test_config_change_data_survives_multiple_emissions() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_surviving_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_config_change_surviving_with_multiple_emissions))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 2"))));
    }

    @Test
    fun test_config_change_data_doesnt_survive_multiple_emissions() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_config_change_observer_doesnt_survive_with_multiple_emissions))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_config_change_not_surviving_with_multiple_emissions))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Config Change Count 1"))));
    }

    @Test
    fun test_config_change_data_survives_2() {

        val scenario = launchFragmentInContainer<UniqueUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_unique_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 3"))))
    }

    @Test
    fun test_config_change_data_survives_doesnt_exceed_5() {

        val scenario = launchFragmentInContainer<UniqueUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_unique_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 5"))))
    }

    @Test
    fun test_config_change_data_doesnt_survive_2() {

        val scenario = launchFragmentInContainer<UniqueUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_unique_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 0"))))
    }

    @Test
    fun test_config_change_data_doesnt_survive_doesnt_exceed_5() {

        val scenario = launchFragmentInContainer<UniqueUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_unique_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_unique_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Unique Update Count 5"))))
    }

    @Test
    fun test_config_change_data_survives_v3() {

        val scenario = launchFragmentInContainer<NoneUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_none_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("None Update Count 3"))))
    }

    @Test
    fun test_config_change_data_survives_exceeds_5() {

        val scenario = launchFragmentInContainer<NoneUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_none_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("None Update Count 6"))))
    }

    @Test
    fun test_config_change_data_doesnt_survive_v3() {

        val scenario = launchFragmentInContainer<NoneUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_none_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("None Update Count 0"))))
    }

    @Test
    fun test_config_change_data_doesnt_survive_doesnt_exceed_5_v3() {

        val scenario = launchFragmentInContainer<NoneUpdateFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(OrientationChangeAction.orientationLandscape())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_invoke_none_observer_not_surviving))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_target_none_not_surviving))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("None Update Count 6"))))
    }

}
