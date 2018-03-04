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
import android.view.View
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
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

    //TODO: currently there is no shadow for support AlertDialog, so no good way to test title/message set correctly

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

        (ShadowDialog.getLatestDialog() as AlertDialog).apply {
            isShowing shouldBe true
            getButton(DialogInterface.BUTTON_POSITIVE).text shouldEqual "OK"
            getButton(DialogInterface.BUTTON_NEUTRAL).text shouldEqual "Later"
            getButton(DialogInterface.BUTTON_NEGATIVE).text shouldEqual "No Thanks"
        }
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

        (ShadowDialog.getLatestDialog() as AlertDialog).apply {
            isShowing shouldBe true
            getButton(DialogInterface.BUTTON_POSITIVE).text shouldEqual activity.getString(android.R.string.ok)
            getButton(DialogInterface.BUTTON_NEUTRAL).text shouldEqual activity.getString(android.R.string.cancel)
            getButton(DialogInterface.BUTTON_NEGATIVE).text shouldEqual activity.getString(android.R.string.no)
        }
    }

    @Test
    fun canSetPositiveButtonClickListener() {
        val listener: DialogInterface.OnClickListener = mock()
        val dialog = builder
                .positiveButtonListener(listener)
                .build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_POSITIVE)

        verify(listener).onClick(any(), eq(DialogInterface.BUTTON_POSITIVE))
    }

    private fun clickDialogButton(button: Int) {
        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        alertDialog.getButton(button).performClick()
    }

    @Test
    fun canSetNeutralButtonClickListener() {
        val listener: DialogInterface.OnClickListener = mock()
        val dialog = builder
                .neutralButtonListener(listener)
                .build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_NEUTRAL)

        verify(listener).onClick(any(), eq(DialogInterface.BUTTON_NEUTRAL))
    }

    @Test
    fun canSetNegativeButtonClickListener() {
        val listener: DialogInterface.OnClickListener = mock()
        val dialog = builder
                .negativeButtonListener(listener)
                .build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_NEGATIVE)

        verify(listener).onClick(any(), eq(DialogInterface.BUTTON_NEGATIVE))
    }

    @Test
    fun canSetDismissListener() {
        val listener: DialogInterface.OnDismissListener = mock()
        val dialog = builder
                .dismissListener(listener)
                .build(activity)

        dialog.show()
        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        alertDialog.dismiss()

        verify(listener).onDismiss(any())
    }

    @Test
    fun buildWithStringsViaConstructor() {
        val dialog = SimpleDialogBuilder(
                title = "Title",
                message = "Message",
                positiveButton = "OK",
                neutralButton = "Later",
                negativeButton = "No Thanks"
        ).build(activity)

        dialog.show()

        val latestDialog = ShadowDialog.getLatestDialog()
        latestDialog.isShowing shouldBe true
    }

    @Test
    fun buildWithResourceIdsViaConstructor() {
        val dialog = SimpleDialogBuilder(
                titleRes = android.R.string.dialog_alert_title,
                messageRes = android.R.string.yes,
                positiveButtonRes = android.R.string.ok,
                neutralButtonRes = android.R.string.cancel,
                negativeButtonRes = android.R.string.no
        ).build(activity)

        dialog.show()

        val latestDialog = ShadowDialog.getLatestDialog()
        latestDialog.isShowing shouldBe true
    }

    @Test
    fun canSetPositiveButtonClickListenerViaConstructor() {
        val listener: DialogInterface.OnClickListener = mock()
        val dialog = SimpleDialogBuilder(positiveButtonListener = listener).build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_POSITIVE)

        verify(listener).onClick(any(), eq(DialogInterface.BUTTON_POSITIVE))
    }

    @Test
    fun canSetNeutralButtonClickListenerViaConstructor() {
        val listener: DialogInterface.OnClickListener = mock()
        val dialog = SimpleDialogBuilder(neutralButtonListener = listener).build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_NEUTRAL)

        verify(listener).onClick(any(), eq(DialogInterface.BUTTON_NEUTRAL))
    }

    @Test
    fun canSetNegativeButtonClickListenerViaConstructor() {
        val listener: DialogInterface.OnClickListener = mock()
        val dialog = SimpleDialogBuilder(negativeButtonListener = listener).build(activity)

        dialog.show()
        clickDialogButton(DialogInterface.BUTTON_NEGATIVE)

        verify(listener).onClick(any(), eq(DialogInterface.BUTTON_NEGATIVE))
    }

    @Test
    fun canSetDismissListenerViaConstructor() {
        val listener: DialogInterface.OnDismissListener = mock()
        val dialog = SimpleDialogBuilder(dismissListener = listener).build(activity)

        dialog.show()
        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        alertDialog.dismiss()

        verify(listener).onDismiss(any())
    }

    @Test
    fun withoutNegativeButton_removesNegativeButton() {
        val dialog = builder.withoutNegativeButton().build(activity)
        dialog.show()

        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        negativeButton.visibility shouldEqual View.GONE
        neutralButton.visibility shouldEqual View.VISIBLE
    }

    @Test
    fun withoutNeutralButton_removesNeutralButton() {
        val dialog = builder.withoutNeutralButton().build(activity)
        dialog.show()

        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        negativeButton.visibility shouldEqual View.VISIBLE
        neutralButton.visibility shouldEqual View.GONE
    }

    @Test
    fun withoutNegativeButtonViaConstructor() {
        val dialog = SimpleDialogBuilder(withoutNegativeButton = true).build(activity)
        dialog.show()

        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        negativeButton.visibility shouldEqual View.GONE
        neutralButton.visibility shouldEqual View.VISIBLE
    }

    @Test
    fun withoutNeutralButtonViaConstructor() {
        val dialog = SimpleDialogBuilder(withoutNeutralButton = true).build(activity)
        dialog.show()

        val alertDialog = ShadowAlertDialog.getLatestDialog() as AlertDialog
        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        negativeButton.visibility shouldEqual View.VISIBLE
        neutralButton.visibility shouldEqual View.GONE
    }

    @Test
    fun callbackIsCalledAfterPositiveButtonClick() {
        val result = clickWithCallback(DialogInterface.BUTTON_POSITIVE)
        result shouldEqual DialogResult.POSITIVE
    }

    @Test
    fun callbackIsCalledAfterNeutralButtonClick() {
        val result = clickWithCallback(DialogInterface.BUTTON_NEUTRAL)
        result shouldEqual DialogResult.NEUTRAL
    }

    @Test
    fun callbackIsCalledAfterNegativeButtonClick() {
        val result = clickWithCallback(DialogInterface.BUTTON_NEGATIVE)
        result shouldEqual DialogResult.NEGATIVE
    }

    private fun clickWithCallback(button: Int): DialogResult? {
        var result: DialogResult? = null

        val dialog = SimpleDialogBuilder(callback = { r -> result = r }).build(activity)

        dialog.show()
        clickDialogButton(button)

        return result
    }

    interface Dummy {
        fun call()
    }

    @Test
    fun resolvesPromiseOnPositiveClick() {
        val callback: Dummy = mock()
        val dialog = SimpleDialogBuilder().build(activity)
        dialog.show().onPositive { callback.call() }

        clickDialogButton(DialogInterface.BUTTON_POSITIVE)

        verify(callback).call()
    }

    @Test
    fun resolvesPromiseOnNeutralClick() {
        val callback: Dummy = mock()
        val dialog = SimpleDialogBuilder().build(activity)
        dialog.show().onNeutral { callback.call() }

        clickDialogButton(DialogInterface.BUTTON_NEUTRAL)

        verify(callback).call()
    }

    @Test
    fun resolvesPromiseOnNegativeClick() {
        val callback: Dummy = mock()
        val dialog = SimpleDialogBuilder().build(activity)
        dialog.show().onNegative { callback.call() }

        clickDialogButton(DialogInterface.BUTTON_NEGATIVE)

        verify(callback).call()
    }
}
