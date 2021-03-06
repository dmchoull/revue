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
import com.github.dmchoull.revue.storage.LocalStorage

internal class InMemoryStorage : LocalStorage {
    private val storage: MutableMap<String, Any> = mutableMapOf()
    private var initialized = false

    fun setTestValues(vararg pairs: Pair<String, Any>) {
        storage.putAll(from = mapOf(*pairs))
    }

    override fun init(context: Context) {
        initialized = true
    }

    override fun setInt(key: String, value: Int) {
        checkInitialized()
        storage[key] = value
    }

    override fun getInt(key: String, default: Int): Int {
        checkInitialized()
        return storage[key] as? Int ?: default
    }

    override fun setString(key: String, value: String) {
        checkInitialized()
        storage[key] = value
    }

    override fun getString(key: String, default: String): String {
        checkInitialized()
        return storage[key] as? String ?: default
    }

    private fun checkInitialized() {
        if (!initialized) throw IllegalStateException()
    }
}
