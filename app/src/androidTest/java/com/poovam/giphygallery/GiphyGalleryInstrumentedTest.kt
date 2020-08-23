package com.poovam.giphygallery

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.poovam.giphygallery", appContext.packageName)
    }
}

//TODO clicking Favourite in trending should show up in fragments
//Clicking unfavourite in favourite should reduce count
//clicking unfavourite in trending should reduce count
//clicking unfavourite in favourites should change state in trending
//test empty view

//clicking favourite in trending should change button state
//searching should make swiperefreshing true
//refreshing should make swiperefresh true
//test pagination?