package com.xrest.buysell

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.xrest.buysell.Activity.MainActivity
import org.junit.Rule
import org.junit.Test

class Registration {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun checkArithmeticUI() {
        Espresso.onView(ViewMatchers.withId(R.id.fb))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.name))
            .perform(ViewActions.typeText("Nabin Kutu"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.phone))
            .perform(ViewActions.typeText("6546546548"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(ViewActions.typeText("nabing"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.password))
            .perform(ViewActions.typeText("nabin123"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.cpassword))
            .perform(ViewActions.typeText("nabin123"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.register))
            .perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.answer))
            .perform(ViewActions.typeText("asd"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.next))
            .perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.answer))
            .perform(ViewActions.typeText("asd"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.next))
            .perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.answer))
            .perform(ViewActions.typeText("asd"))
        Espresso.closeSoftKeyboard()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.next))
            .perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.
        onView(ViewMatchers.withId(R.id.username))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}