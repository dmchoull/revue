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

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import com.github.dmchoull.revue.R
import com.github.dmchoull.revue.dialog.RevueDialog
import com.github.dmchoull.revue.dialog.SimpleRevueDialog
import com.github.dmchoull.revue.util.DialogResultPromise
import java.lang.ref.WeakReference

@Suppress("MemberVisibilityCanBePrivate")
open class SimpleDialogBuilder(
        callback: DialogResultCallback? = null,
        private var title: String? = null,
        private var titleRes: Int = R.string.default_rate_title,
        private var message: String? = null,
        private var messageRes: Int = R.string.default_rate_message,
        private var positiveButton: String? = null,
        private var positiveButtonRes: Int = R.string.default_positive_btn,
        positiveButtonListener: DialogInterface.OnClickListener? = null,
        private var neutralButton: String? = null,
        private var neutralButtonRes: Int = R.string.default_neutral_btn,
        neutralButtonListener: DialogInterface.OnClickListener? = null,
        private var negativeButton: String? = null,
        private var negativeButtonRes: Int = R.string.default_negative_btn,
        negativeButtonListener: DialogInterface.OnClickListener? = null,
        dismissListener: DialogInterface.OnDismissListener? = null,
        private var withoutNegativeButton: Boolean = false,
        private var withoutNeutralButton: Boolean = false
) : RevueDialogBuilder {
    private var _callback = WeakReference(callback)
    private var _positiveButtonListener = WeakReference(positiveButtonListener)
    private var _neutralButtonListener = WeakReference(neutralButtonListener)
    private var _negativeButtonListener = WeakReference(negativeButtonListener)
    private var _dismissListener = WeakReference(dismissListener)

    private val promise = DialogResultPromise()

    override fun callback(dialogResultCallback: DialogResultCallback) =
            apply { _callback = WeakReference(dialogResultCallback) }

    fun title(title: String) = apply { this.title = title }

    fun title(@StringRes title: Int) = apply { this.titleRes = title }

    fun message(message: String) = apply { this.message = message }

    fun message(@StringRes message: Int) = apply { this.messageRes = message }

    @JvmOverloads
    fun positiveButton(positiveButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.positiveButton = positiveButton
                _positiveButtonListener = WeakReference(listener)
            }

    @JvmOverloads
    fun positiveButton(@StringRes positiveButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.positiveButtonRes = positiveButton
                _positiveButtonListener = WeakReference(listener)
            }

    fun positiveButtonListener(listener: DialogInterface.OnClickListener) =
            apply { _positiveButtonListener = WeakReference(listener) }

    @JvmOverloads
    fun neutralButton(neutralButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.neutralButton = neutralButton
                _neutralButtonListener = WeakReference(listener)
            }

    @JvmOverloads
    fun neutralButton(@StringRes neutralButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.neutralButtonRes = neutralButton
                _neutralButtonListener = WeakReference(listener)
            }

    fun neutralButtonListener(listener: DialogInterface.OnClickListener) =
            apply { _neutralButtonListener = WeakReference(listener) }

    @JvmOverloads
    fun negativeButton(negativeButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.negativeButton = negativeButton
                _negativeButtonListener = WeakReference(listener)
            }

    @JvmOverloads
    fun negativeButton(@StringRes negativeButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.negativeButtonRes = negativeButton
                _negativeButtonListener = WeakReference(listener)
            }

    fun negativeButtonListener(listener: DialogInterface.OnClickListener) =
            apply { _negativeButtonListener = WeakReference(listener) }

    fun dismissListener(listener: DialogInterface.OnDismissListener) =
            apply { _dismissListener = WeakReference(listener) }

    fun withoutNegativeButton() = apply { this.withoutNegativeButton = true }

    fun withoutNeutralButton() = apply { this.withoutNeutralButton = true }

    override fun build(context: Context): RevueDialog {
        val dialogBuilder = AlertDialog.Builder(context)

        setDialogTitle(dialogBuilder)
        setDialogMessage(dialogBuilder)
        setDialogPositiveButton(dialogBuilder)
        setDialogNeutralButton(dialogBuilder)
        setDialogNegativeButton(dialogBuilder)
        setDismissListener(dialogBuilder)

        val alertDialog = dialogBuilder.create()
        return SimpleRevueDialog(alertDialog, promise)
    }

    private fun setDialogTitle(dialogBuilder: AlertDialog.Builder) {
        when (title) {
            is String -> dialogBuilder.setTitle(title)
            else -> dialogBuilder.setTitle(titleRes)
        }
    }

    private fun setDialogMessage(dialogBuilder: AlertDialog.Builder) {
        when (message) {
            is String -> dialogBuilder.setMessage(message)
            else -> dialogBuilder.setMessage(messageRes)
        }
    }

    private fun setDialogPositiveButton(dialogBuilder: AlertDialog.Builder) {
        val listener = dialogClickListener(_positiveButtonListener.get(), { onPositiveClick() })

        when (positiveButton) {
            is String -> dialogBuilder.setPositiveButton(positiveButton, listener)
            else -> dialogBuilder.setPositiveButton(positiveButtonRes, listener)
        }
    }

    private fun onPositiveClick() {
        _callback.get()?.onResult(DialogResult.POSITIVE)
        promise.resolve(DialogResult.POSITIVE)
    }

    private fun setDialogNeutralButton(dialogBuilder: AlertDialog.Builder) {
        if (withoutNeutralButton) return

        val listener = dialogClickListener(_neutralButtonListener.get(), { onNeutralClick() })

        when (neutralButton) {
            is String -> dialogBuilder.setNeutralButton(neutralButton, listener)
            else -> dialogBuilder.setNeutralButton(neutralButtonRes, listener)
        }
    }

    private fun onNeutralClick() {
        _callback.get()?.onResult(DialogResult.NEUTRAL)
        promise.resolve(DialogResult.NEUTRAL)
    }

    private fun setDialogNegativeButton(dialogBuilder: AlertDialog.Builder) {
        if (withoutNegativeButton) return

        val listener = dialogClickListener(_negativeButtonListener.get(), { onNegativeClick() })

        when (negativeButton) {
            is String -> dialogBuilder.setNegativeButton(negativeButton, listener)
            else -> dialogBuilder.setNegativeButton(negativeButtonRes, listener)
        }
    }

    private fun onNegativeClick() {
        _callback.get()?.onResult(DialogResult.NEGATIVE)
        promise.resolve(DialogResult.NEGATIVE)
    }

    private fun setDismissListener(dialogBuilder: AlertDialog.Builder) {
        val listener = _dismissListener.get()
        if (listener != null) {
            dialogBuilder.setOnDismissListener(listener)
        }
    }
}

private fun dialogClickListener(listener: DialogInterface.OnClickListener?, action: () -> Unit) =
        { dialog: DialogInterface, which: Int ->
            listener?.onClick(dialog, which)
            action()
        }
