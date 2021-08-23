package com.xrest.buysell

import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.Activity.MainActivity
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.DatagramChannel.open

class UpdateProfile {
    @get:Rule
    val testRule = ActivityScenarioRule(Dashboard::class.java)
    @Test
    fun checkArithmeticUI() {
        runBlocking {
            var userRepository = UserRepository()
            RetroftiService.token = "Bearer " + userRepository.login("456", "456").token
            RetroftiService.users = userRepository.login("456", "456").user
        }
        onView(withId(R.id.navView)).perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.profile))
            .perform(ViewActions.click())
//
////        Espresso.onView(ViewMatchers.withId(R.id.password))
////            .perform(ViewActions.typeText("asd"))
////        Thread.sleep(1000)
////        Espresso.closeSoftKeyboard()
////        Espresso.onView(ViewMatchers.withId(R.id.sign))
////            .perform(ViewActions.click())
////        Thread.sleep(4000)
////        Espresso.
////        onView(ViewMatchers.withId(R.id.fl))
////            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))       Thread.sleep(1000)
    }
}