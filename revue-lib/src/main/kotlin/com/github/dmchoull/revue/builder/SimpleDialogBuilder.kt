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
import android.content.Intent
import android.net.Uri
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import com.github.dmchoull.revue.R
import com.github.dmchoull.revue.dialog.RevueDialog
import com.github.dmchoull.revue.dialog.SimpleRevueDialog

@Suppress("MemberVisibilityCanBePrivate")
class SimpleDialogBuilder(
        private var callback: DialogResultCallback = {},
        private var title: String? = null,
        private var titleRes: Int = R.string.default_rate_title,
        private var message: String? = null,
        private var messageRes: Int = R.string.default_rate_message,
        private var positiveButton: String? = null,
        private var positiveButtonRes: Int = R.string.default_positive_btn,
        private var positiveButtonListener: DialogInterface.OnClickListener? = null,
        private var neutralButton: String? = null,
        private var neutralButtonRes: Int = R.string.default_neutral_btn,
        private var neutralButtonListener: DialogInterface.OnClickListener? = null,
        private var negativeButton: String? = null,
        private var negativeButtonRes: Int = R.string.default_negative_btn,
        private var negativeButtonListener: DialogInterface.OnClickListener? = null,
        private var dismissListener: DialogInterface.OnDismissListener? = null,
        private var withoutNegativeButton: Boolean = false,
        private var withoutNeutralButton: Boolean = false
) : RevueDialogBuilder {
    override fun callback(f: DialogResultCallback) = apply { callback = f }

    fun title(title: String) = apply { this.title = title }

    fun title(@StringRes title: Int) = apply { this.titleRes = title }

    fun message(message: String) = apply { this.message = message }

    fun message(@StringRes message: Int) = apply { this.messageRes = message }

    @JvmOverloads
    fun positiveButton(positiveButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.positiveButton = positiveButton
                this.positiveButtonListener = listener
            }

    @JvmOverloads
    fun positiveButton(@StringRes positiveButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.positiveButtonRes = positiveButton
                this.positiveButtonListener = listener
            }

    fun positiveButtonListener(listener: DialogInterface.OnClickListener) = apply { positiveButtonListener = listener }

    @JvmOverloads
    fun neutralButton(neutralButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.neutralButton = neutralButton
                this.neutralButtonListener = listener
            }

    @JvmOverloads
    fun neutralButton(@StringRes neutralButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.neutralButtonRes = neutralButton
                this.neutralButtonListener = listener
            }

    fun neutralButtonListener(listener: DialogInterface.OnClickListener) = apply { neutralButtonListener = listener }

    @JvmOverloads
    fun negativeButton(negativeButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.negativeButton = negativeButton
                this.negativeButtonListener = listener
            }

    @JvmOverloads
    fun negativeButton(@StringRes negativeButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.negativeButtonRes = negativeButton
                this.negativeButtonListener = listener
            }

    fun negativeButtonListener(listener: DialogInterface.OnClickListener) = apply { negativeButtonListener = listener }

    fun dismissListener(listener: DialogInterface.OnDismissListener) = apply { dismissListener = listener }

    fun withoutNegativeButton() = apply { this.withoutNegativeButton = true }

    fun withoutNeutralButton() = apply { this.withoutNeutralButton = true }

    override fun build(context: Context): RevueDialog {
        val dialogBuilder = AlertDialog.Builder(context)

        setDialogTitle(dialogBuilder)
        setDialogMessage(dialogBuilder)
        setDialogPositiveButton(dialogBuilder, context)
        setDialogNeutralButton(dialogBuilder)
        setDialogNegativeButton(dialogBuilder)
        setDismissListener(dialogBuilder)

        val alertDialog = dialogBuilder.create()
        return SimpleRevueDialog(alertDialog)
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

    private fun setDialogPositiveButton(dialogBuilder: AlertDialog.Builder, context: Context) {
        val listener = dialogClickListener(positiveButtonListener, { onPositiveClick(context) })

        when (positiveButton) {
            is String -> dialogBuilder.setPositiveButton(positiveButton, listener)
            else -> dialogBuilder.setPositiveButton(positiveButtonRes, listener)
        }
    }

    private fun onPositiveClick(context: Context) {
        callback(DialogResult.POSITIVE)
        openAppInStore(context)
    }

    private fun openAppInStore(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        val appPackage = context.packageName
        intent.data = Uri.parse("market://details?id=$appPackage")
        context.startActivity(intent)
    }

    private fun setDialogNeutralButton(dialogBuilder: AlertDialog.Builder) {
        if (withoutNeutralButton) return

        val listener = dialogClickListener(neutralButtonListener, this::onNeutralClick)

        when (neutralButton) {
            is String -> dialogBuilder.setNeutralButton(neutralButton, listener)
            else -> dialogBuilder.setNeutralButton(neutralButtonRes, listener)
        }
    }

    private fun onNeutralClick() {
        callback(DialogResult.NEUTRAL)
    }

    private fun setDialogNegativeButton(dialogBuilder: AlertDialog.Builder) {
        if (withoutNegativeButton) return

        val listener = dialogClickListener(negativeButtonListener, this::onNegativeClick)

        when (negativeButton) {
            is String -> dialogBuilder.setNegativeButton(negativeButton, listener)
            else -> dialogBuilder.setNegativeButton(negativeButtonRes, listener)
        }
    }

    private fun onNegativeClick() {
        callback(DialogResult.NEGATIVE)
    }

    private fun setDismissListener(dialogBuilder: AlertDialog.Builder) {
        val listener = dismissListener
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
