package eu.darken.androidstarter.main.fragment

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.darken.androidstarter.main.ui.fragment.ExampleFragment
import eu.darken.androidstarter.main.ui.fragment.ExampleFragmentVM
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import testhelper.BaseUITest
import testhelper.launchFragmentInHiltContainer

@HiltAndroidTest
class ExampleFragmentTest : BaseUITest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @BindValue
    val mockViewModel = mockk<ExampleFragmentVM>(relaxed = true)

    @Before fun init() {
        hiltRule.inject()

        mockViewModel.apply {
            every { state } returns liveData { }
        }
    }

    @Test fun happyPath() {
        launchFragmentInHiltContainer<ExampleFragment>()

        verify { mockViewModel.state }
    }
}