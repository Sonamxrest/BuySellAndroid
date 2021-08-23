package com.xrest.buysell

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.xrest.buysell.Activity.MainActivity
import org.junit.Rule
import org.junit.Test

class Login {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun checkArithmeticUI() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(ViewActions.typeText("sonam"))
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("sonam"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.sign))
            .perform(ViewActions.click())
        Thread.sleep(4000)
        Espresso.
        onView(ViewMatchers.withId(R.id.navView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}