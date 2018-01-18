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
import android.support.v7.app.AlertDialog
import com.github.dmchoull.revue.R
import com.github.dmchoull.revue.dialog.RevueDialog
import com.github.dmchoull.revue.dialog.SimpleRevueDialog

@Suppress("MemberVisibilityCanBePrivate")
class SimpleDialogBuilder : RevueDialogBuilder {
    var title: String? = null
        private set

    var titleRes: Int? = null
        private set

    var message: String? = null
        private set

    var messageRes: Int? = null
        private set

    var positiveButton: String? = null
        private set

    var positiveButtonRes: Int? = null
        private set

    var neutralButton: String? = null
        private set

    var neutralButtonRes: Int? = null
        private set

    var negativeButton: String? = null
        private set

    var negativeButtonRes: Int? = null
        private set

    private var positiveButtonListener: DialogInterface.OnClickListener? = null

    private var neutralButtonListener: DialogInterface.OnClickListener? = null

    private var negativeButtonListener: DialogInterface.OnClickListener? = null

    fun title(title: String) = apply { this.title = title }

    fun title(title: Int) = apply { this.titleRes = title }

    fun message(message: String) = apply { this.message = message }

    fun message(message: Int) = apply { this.messageRes = message }

    @JvmOverloads
    fun positiveButton(positiveButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.positiveButton = positiveButton
                this.positiveButtonListener = listener
            }

    @JvmOverloads
    fun positiveButton(positiveButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.positiveButtonRes = positiveButton
                this.positiveButtonListener = listener
            }

    @JvmOverloads
    fun neutralButton(neutralButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.neutralButton = neutralButton
                this.neutralButtonListener = listener
            }

    @JvmOverloads
    fun neutralButton(neutralButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.neutralButtonRes = neutralButton
                this.neutralButtonListener = listener
            }

    @JvmOverloads
    fun negativeButton(negativeButton: String, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.negativeButton = negativeButton
                this.negativeButtonListener = listener
            }

    @JvmOverloads
    fun negativeButton(negativeButton: Int, listener: DialogInterface.OnClickListener? = null) =
            apply {
                this.negativeButtonRes = negativeButton
                this.negativeButtonListener = listener
            }

    override fun build(context: Context): RevueDialog {
        val dialogBuilder = AlertDialog.Builder(context)

        setDialogTitle(dialogBuilder)
        setDialogMessage(dialogBuilder)
        setDialogPositiveButton(dialogBuilder)
        setDialogNeutralButton(dialogBuilder)
        setDialogNegativeButton(dialogBuilder)

        val alertDialog = dialogBuilder.create()
        return SimpleRevueDialog(alertDialog)
    }

    private fun setDialogTitle(dialogBuilder: AlertDialog.Builder) {
        val title = titleRes ?: title ?: ""

        when (title) {
            is Int -> dialogBuilder.setTitle(title)
            is String -> dialogBuilder.setTitle(title)
        }
    }

    private fun setDialogMessage(dialogBuilder: AlertDialog.Builder) {
        val message = messageRes ?: message ?: R.string.default_rate_message

        when (message) {
            is Int -> dialogBuilder.setMessage(message)
            is String -> dialogBuilder.setMessage(message)
        }
    }

    private fun setDialogPositiveButton(dialogBuilder: AlertDialog.Builder) {
        val positiveButton = positiveButtonRes ?: positiveButton ?: R.string.default_positive_btn

        when (positiveButton) {
            is Int -> dialogBuilder.setPositiveButton(positiveButton, positiveButtonListener)
            is String -> dialogBuilder.setPositiveButton(positiveButton, positiveButtonListener)
        }
    }

    private fun setDialogNeutralButton(dialogBuilder: AlertDialog.Builder) {
        val neutralButton = neutralButtonRes ?: neutralButton ?: R.string.default_neutral_btn

        when (neutralButton) {
            is Int -> dialogBuilder.setNeutralButton(neutralButton, neutralButtonListener)
            is String -> dialogBuilder.setNeutralButton(neutralButton, neutralButtonListener)
        }
    }

    private fun setDialogNegativeButton(dialogBuilder: AlertDialog.Builder) {
        val negativeButton = negativeButtonRes ?: negativeButton ?: R.string.default_negative_btn

        when (negativeButton) {
            is Int -> dialogBuilder.setNegativeButton(negativeButton, negativeButtonListener)
            is String -> dialogBuilder.setNegativeButton(negativeButton, negativeButtonListener)
        }
    }
}
