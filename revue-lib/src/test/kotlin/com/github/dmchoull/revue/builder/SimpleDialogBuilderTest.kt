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
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowAlertDialog
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
                .positiveButton("OK")
                .neutralButton("Later")
                .negativeButton("No Thanks")
                .build(activity)

        dialog.show()

        val latestDialog = ShadowDialog.getLatestDialog()
        latestDialog.isShowing shouldBe true
    }

    @Test
    fun buildWithResourceIds() {
        val dialog = builder
                .title(android.R.string.dialog_alert_title)
                .message(android.R.string.yes)
                .positiveButton(android.R.string.ok)
                .neutralButton(android.R.string.cancel)
                .negativeButton(android.R.string.no)
                .build(activity)

        dialog.show()

        val latestDialog = ShadowDialog.getLatestDialog()
        latestDialog.isShowing shouldBe true
    }

    @Test
    fun canSetPositiveButtonClickListener() {
        val dummy: Dummy = mock()

        val dialog = builder
                .positiveButton("OK", DialogInterface.OnClickListener { _, _ -> dummy.doSomething() })
                .build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_POSITIVE)

        verify(dummy).doSomething()
    }

    private fun clickDialogButton(button: Int) {
        val alertDialog: AlertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        alertDialog.getButton(button).performClick()
    }

    @Test
    fun canSetNeutralButtonClickListener() {
        val dummy: Dummy = mock()

        val dialog = builder
                .neutralButton("Later", DialogInterface.OnClickListener { _, _ -> dummy.doSomething() })
                .build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_NEUTRAL)

        verify(dummy).doSomething()
    }

    @Test
    fun canSetNegativeButtonClickListener() {
        val dummy: Dummy = mock()

        val dialog = builder
                .negativeButton("No", DialogInterface.OnClickListener { _, _ -> dummy.doSomething() })
                .build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_NEGATIVE)

        verify(dummy).doSomething()
    }
}

interface Dummy {
    fun doSomething()
}
