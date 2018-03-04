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
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class DialogResultPromiseTest {
    private lateinit var positiveCallback: () -> Unit
    private lateinit var negativeCallback: () -> Unit
    private lateinit var neutralCallback: () -> Unit
    private lateinit var promise: DialogResultPromise

    @BeforeEach
    fun setUp() {
        positiveCallback = mock()
        negativeCallback = mock()
        neutralCallback = mock()

        promise = DialogResultPromise()
        promise.onPositive(positiveCallback)
        promise.onNegative(negativeCallback)
        promise.onNeutral(neutralCallback)
    }

    @Test
    @DisplayName("calls positive callback when resolved with a positive result")
    fun onPositive() {
        promise.resolve(DialogResult.POSITIVE)

        verify(positiveCallback).invoke()
        verify(negativeCallback, never()).invoke()
        verify(neutralCallback, never()).invoke()
    }

    @Test
    @DisplayName("calls negative callback when resolved with a negative result")
    fun onNegative() {
        promise.resolve(DialogResult.NEGATIVE)

        verify(positiveCallback, never()).invoke()
        verify(negativeCallback).invoke()
        verify(neutralCallback, never()).invoke()
    }

    @Test
    @DisplayName("calls neutral callback when resolved with a neutral result")
    fun onNeutral() {
        promise.resolve(DialogResult.NEUTRAL)

        verify(positiveCallback, never()).invoke()
        verify(negativeCallback, never()).invoke()
        verify(neutralCallback).invoke()
    }

    @Test
    @DisplayName("does not attempt to call null callbacks")
    fun nullCallbacks() {
        val promise = DialogResultPromise()
        promise.resolve(DialogResult.POSITIVE)
        promise.resolve(DialogResult.NEGATIVE)
        promise.resolve(DialogResult.NEUTRAL)
    }
}
