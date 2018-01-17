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
import com.github.dmchoull.revue.builder.SimpleDialogBuilder
import com.github.dmchoull.revue.dialog.RevueDialog
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class RevueTest {
    private lateinit var context: Context
    private lateinit var dialog: RevueDialog
    private lateinit var revue: Revue

    @BeforeEach
    fun setUp() {
        context = mock()
        dialog = mock()
        revue = Revue()
    }

    @Test
    @DisplayName("uses SimpleDialogBuilder by default")
    fun defaultSimpleDialogBuilder() {
        revue.dialogBuilder shouldBeInstanceOf SimpleDialogBuilder::class
    }

    @Test
    @DisplayName("showNow immediately builds and shows a dialog")
    fun showNow() {
        revue.dialogBuilder = mock {
            on { build(context) } doReturn dialog
        }

        revue.showNow(context)

        verify(dialog).show()
    }
}
