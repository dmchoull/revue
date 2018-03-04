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

package com.github.dmchoull.revue.util

import com.github.dmchoull.revue.builder.DialogResult

class DialogResultPromise {
    private var _positiveCallback: (() -> Unit)? = null
    private var _negativeCallback: (() -> Unit)? = null
    private var _neutralCallback: (() -> Unit)? = null

    fun onPositive(callback: () -> Unit) = apply { _positiveCallback = callback }

    fun onNegative(callback: () -> Unit) = apply { _negativeCallback = callback }

    fun onNeutral(callback: () -> Unit) = apply { _neutralCallback = callback }

    fun resolve(result: DialogResult) {
        when (result) {
            DialogResult.POSITIVE -> _positiveCallback?.invoke()
            DialogResult.NEGATIVE -> _negativeCallback?.invoke()
            DialogResult.NEUTRAL -> _neutralCallback?.invoke()
        }
    }
}
