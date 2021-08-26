package com.xrest.buysell

import android.R
import android.view.Gravity
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test


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

        onView(withId(com.xrest.buysell.R.id.dl)).perform(DrawerActions.open());



//        onView(withId(com.xrest.buysell.R.id.container))
//            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
//            .perform(DrawerActions.open()) // Open Drawer


        // Start the screen of your activity.

        // Start the screen of your activity.
        onView(withId(com.xrest.buysell.R.id.navView))
            .perform(NavigationViewActions.navigateTo(com.xrest.buysell.R.id.profile))
Thread.sleep(1000)
        onView(withId(com.xrest.buysell.R.id.dl)).perform(DrawerActions.close());
        onView(withId(com.xrest.buysell.R.id.update)).perform(ViewActions.click())
        onView(withId(com.xrest.buysell.R.id.name)).perform(ViewActions.typeText("Sonam Shrestha"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()
        onView(withId(com.xrest.buysell.R.id.number)).perform(ViewActions.typeText("9818780177"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()
        onView(withId(com.xrest.buysell.R.id.number)).perform(ViewActions.typeText("456"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()
        onView(withId(com.xrest.buysell.R.id.update)).perform(ViewActions.click())
        onView(withId(com.xrest.buysell.R.id.update)).check(matches(isDisplayed()))
        // Check that you Activity was opened.

        // Check that you Activity was opened.
//        val expectedNoStatisticsText: String = InstrumentationRegistry.getTargetContext()
//            .getString(R.string.no_item_available)
        //onView(withId(R.id.no_statistics)).check(matches(withText(expectedNoStatisticsText)))
}
}
//fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription()
//        : String {
//    var description = ""
//    onActivity {
//        description =
//            it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
//    }
//    return description
//}