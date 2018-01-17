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

package com.github.dmchoull.revue.builder

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowDialog

@RunWith(RobolectricTestRunner::class)
class SimpleDialogBuilderTest {
    private lateinit var activity: Activity
    private lateinit var builder: SimpleDialogBuilder

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(AppCompatActivity::class.java)
        builder = SimpleDialogBuilder()
    }

    // TODO: currently there is no shadow for support AlertDialog, so no good way to test title/message set correctly

    @Test
    fun buildWithStrings() {
        val dialog = builder
                .title("Title")
                .message("Message")
                .build(activity)

        dialog.show()

        val latestDialog = ShadowDialog.getLatestDialog()
        latestDialog.isShowing shouldBe true
    }

    @Test
    fun buildWithResourceIds() {
        val dialog = builder
                .title(android.R.string.ok)
                .message(android.R.string.yes)
                .build(activity)

        dialog.show()

        val latestDialog = ShadowDialog.getLatestDialog()
        latestDialog.isShowing shouldBe true
    }
}
