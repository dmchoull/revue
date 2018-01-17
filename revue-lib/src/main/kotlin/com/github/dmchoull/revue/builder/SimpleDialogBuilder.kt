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

    fun title(title: String) = apply { this.title = title }

    fun title(title: Int) = apply { this.titleRes = title }

    fun message(message: String) = apply { this.message = message }

    fun message(message: Int) = apply { this.messageRes = message }

    // TODO: allow setting button labels and listeners

    override fun build(context: Context): RevueDialog {
        val dialogBuilder = AlertDialog.Builder(context)

        setDialogTitle(dialogBuilder)
        setDialogMessage(context, dialogBuilder)

        val alertDialog = dialogBuilder
                .setPositiveButton(R.string.default_positive_btn, null)
                .setNeutralButton(R.string.default_neutral_btn, null)
                .setNegativeButton(R.string.default_negative_btn, null)
                .create()

        return SimpleRevueDialog(alertDialog)
    }

    private fun setDialogTitle(dialogBuilder: AlertDialog.Builder) {
        val title = titleRes ?: title ?: ""
        when (title) {
            is Int -> dialogBuilder.setTitle(title)
            is String -> dialogBuilder.setTitle(title)
        }
    }

    private fun setDialogMessage(context: Context, dialogBuilder: AlertDialog.Builder) {
        val message = messageRes ?: message ?: context.getString(R.string.default_rate_message)
        when (message) {
            is Int -> dialogBuilder.setMessage(message)
            is String -> dialogBuilder.setMessage(message)
        }
    }
}
