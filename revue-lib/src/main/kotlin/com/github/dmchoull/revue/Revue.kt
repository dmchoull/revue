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

package com.github.dmchoull.revue

import android.content.Context
import com.github.dmchoull.revue.builder.RevueDialogBuilder
import com.github.dmchoull.revue.builder.SimpleDialogBuilder
import com.github.dmchoull.revue.storage.LocalStorage
import com.github.dmchoull.revue.storage.SharedPreferencesStorage

const val TIMES_LAUNCHED_KEY = "REVUE_TIMES_LAUNCHED"
const val DEFAULT_TIMES_LAUNCHED = 3

class Revue(private val localStorage: LocalStorage = SharedPreferencesStorage()) {
    var dialogBuilder: RevueDialogBuilder = SimpleDialogBuilder()
    var config = RevueConfig()

    fun init(context: Context) {
        localStorage.init(context)

        val timesLaunched = localStorage.getInt(TIMES_LAUNCHED_KEY, default = 0)
        localStorage.setInt(TIMES_LAUNCHED_KEY, timesLaunched + 1)
    }

    fun showNow(context: Context) {
        showDialog(context)
    }

    fun request(context: Context): Boolean {
        if (localStorage.getInt(TIMES_LAUNCHED_KEY, default = 0) >= config.timesLaunched) {
            showDialog(context)
            return true
        }

        return false
    }

    private fun showDialog(context: Context) {
        dialogBuilder.build(context).show()
    }
}

data class RevueConfig(val timesLaunched: Int = DEFAULT_TIMES_LAUNCHED)
