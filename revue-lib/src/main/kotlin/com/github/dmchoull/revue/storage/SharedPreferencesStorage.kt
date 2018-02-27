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

import android.content.Context
import android.content.SharedPreferences
import java.lang.IllegalStateException

private const val REVUE_PREFS = "REVUE_PREFS"

class SharedPreferencesStorage : LocalStorage {
    private var preferences: SharedPreferences? = null

    override fun init(context: Context) {
        preferences = context.getSharedPreferences(REVUE_PREFS, Context.MODE_PRIVATE)
    }

    override fun setInt(key: String, value: Int) {
        val prefs = getPrefs()
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun getInt(key: String, default: Int): Int =
            getPrefs().getInt(key, default)

    override fun setString(key: String, value: String) {
        val prefs = getPrefs()
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getString(key: String, default: String): String =
            getPrefs().getString(key, default)

    private fun getPrefs() = preferences
            ?: throw IllegalStateException("This instance of Revue has not been properly initialized. Make sure you call Revue::init when your app is launched")
}
