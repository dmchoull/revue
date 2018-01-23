/*
 * MIT License
 *
 * Copyright (c) 2018 David McHoull
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.dmchoull.revue.storage

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.lang.IllegalStateException

@RunWith(RobolectricTestRunner::class)
internal class SharedPreferencesStorageTest {
    private lateinit var activity: Activity
    private lateinit var storage: SharedPreferencesStorage

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(AppCompatActivity::class.java)
        storage = SharedPreferencesStorage()
    }

    @Test(expected = IllegalStateException::class)
    fun setIntWithoutInitThrowsException() {
        storage.setInt("TEST", 1)
    }

    @Test(expected = IllegalStateException::class)
    fun getIntWithoutInitThrowsException() {
        storage.getInt("TEST", default = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun setStringWithoutInitThrowsException() {
        storage.setString("TEST", "foo")
    }

    @Test(expected = IllegalStateException::class)
    fun getStringWithoutInitThrowsException() {
        storage.getString("TEST", default = "foo")
    }

    @Test
    fun setAndGetInt() {
        storage.init(activity)

        storage.setInt("TEST", 1)

        storage.getInt("TEST", default = 0) shouldEqual 1
    }

    @Test
    fun setAndGetString() {
        storage.init(activity)

        storage.setString("TEST", "foo")

        storage.getString("TEST", default = "bar") shouldEqual "foo"
    }

    @Test
    fun getIntReturnsDefaultIfNotSet() {
        storage.init(activity)

        storage.getInt("TEST", default = 2) shouldEqual 2
    }

    @Test
    fun getStringReturnsDefaultIfNotSet() {
        storage.init(activity)

        storage.getString("TEST", default = "foo") shouldEqual "foo"
    }
}
