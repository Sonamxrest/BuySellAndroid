package com.xrest.buysell

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.xrest.buysell.Activity.Dashboard
import com.xrest.buysell.Adapters.MainProductAdapter
import com.xrest.buysell.Retrofit.Repo.UserRepository
import com.xrest.buysell.Retrofit.RetroftiService
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class Like {
    @get:Rule
    val main = ActivityScenarioRule(Dashboard::class.java)
    @Test
    fun testView() {

        runBlocking {
            var userRepository = UserRepository()
            RetroftiService.token = "Bearer " + userRepository.login("asd", "asd").token
            RetroftiService.users = userRepository.login("asd", "asd").user
        }
        Espresso.onView(withId(R.id.dl)).perform(DrawerActions.open());
        Espresso.onView(withId(R.id.navView))
            .perform(NavigationViewActions.navigateTo(com.xrest.buysell.R.id.admin))
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.dl)).perform(DrawerActions.close());
        Espresso.onView(withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GroupieViewHolder>(
                0,
                clickOnViewChild(R.id.like)
            )
        )
        Thread.sleep(2000)
        Espresso.onView(withId(R.id.rv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
     fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click()
            .perform(uiController, view.findViewById<View>(viewId))
    }
}