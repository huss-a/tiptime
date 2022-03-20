package com.example.tiptime

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorTests {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun calculateDefaultTip() {
        typeCost("50.0")
        clickOn(R.id.calc_button)
        checkTipAmount("9.0")
    }

    @Test
    fun calculate20PercentTip() {
        typeCost("200.0")
        onView(withId(R.id.radio_amazing)).perform(click())
        clickOn(R.id.calc_button)
        checkTipAmount("40.0")
    }

    @Test
    fun calculate15PercentTip() {
        typeCost("220.0")
        clickOn(R.id.radio_ok)
        clickOn(R.id.calc_button)
        checkTipAmount("33.0")
    }

    @Test
    fun checkRounding() {
        typeCost("233.0")
        clickOn(R.id.roundup_switch)
        clickOn(R.id.calc_button)
        checkTipAmount("42.0")
    }

    @Test
    fun emptyInput() {
        clickOn(R.id.calc_button)
        onView(withId(R.id.tip_amount)).check(matches(withText(equalTo(""))))
    }

    private fun clickOn(viewId: Int) {
        onView(withId(viewId)).perform(click())
    }

    private fun typeCost(cost: String) {
        onView(withId(R.id.cost_of_service_input)).perform(typeText(cost))
    }

    private fun checkTipAmount(amount: String) {
        onView(withId(R.id.tip_amount)).check(matches(withText(containsString(amount))))
    }
}