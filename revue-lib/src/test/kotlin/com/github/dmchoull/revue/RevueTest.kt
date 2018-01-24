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
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RevueTest {
    private lateinit var context: Context
    private lateinit var dialog: RevueDialog
    private lateinit var revue: Revue
    private lateinit var storage: InMemoryStorage

    @BeforeEach
    fun setUp() {
        context = mock()
        dialog = mock()
        storage = InMemoryStorage()
        revue = Revue(localStorage = storage)
    }

    @Test
    @DisplayName("uses SimpleDialogBuilder by default")
    fun defaultSimpleDialogBuilder() {
        revue.dialogBuilder shouldBeInstanceOf SimpleDialogBuilder::class
    }

    @Test
    @DisplayName("has default config")
    fun defaultConfig() {
        revue.config shouldEqual RevueConfig(timesLaunched = 3)
    }

    @Test
    @DisplayName("init sets times launched to 1 when not previously set")
    fun initTimesLaunchedFirstCall() {
        revue.init(context)

        storage.getInt(TIMES_LAUNCHED_KEY, default = 0) shouldEqual 1
    }

    @Test
    @DisplayName("init increments times launched")
    fun initTimesLaunched() {
        storage.setTestValues(TIMES_LAUNCHED_KEY to 1)

        revue.init(context)

        storage.getInt(TIMES_LAUNCHED_KEY, default = 0) shouldEqual 2
    }

    @Nested
    @DisplayName("showNow")
    inner class ShowNow {
        @BeforeEach
        fun setUp() {
            revue.dialogBuilder = mock {
                on { build(context) } doReturn dialog
            }
            revue.init(context)
        }

        @Test
        @DisplayName("immediately builds and shows a dialog")
        fun showNow() {
            revue.showNow(context)

            verify(dialog).show()
        }

        @Test
        @DisplayName("resets times launched to zero")
        fun showNowReset() {
            storage.setTestValues(TIMES_LAUNCHED_KEY to 3)

            revue.showNow(context)

            storage.getInt(TIMES_LAUNCHED_KEY, default = 1) shouldEqual 0
        }
    }

    @Nested
    @DisplayName("when conditions are satisfied")
    inner class ConditionsSatisfied {
        @BeforeEach
        fun setUp() {
            revue.dialogBuilder = mock {
                on { build(context) } doReturn dialog
            }
            revue.init(context)
            storage.setTestValues(TIMES_LAUNCHED_KEY to DEFAULT_TIMES_LAUNCHED)
        }

        @Test
        @DisplayName("request shows a dialog")
        fun request() {
            revue.request(context)

            verify(dialog).show()
        }

        @Test
        @DisplayName("request returns true")
        fun requestReturnsTrue() {
            revue.request(context) shouldBe true
        }

        @Test
        @DisplayName("request resets times launched to zero")
        fun requestResets() {
            revue.request(context)

            storage.getInt(TIMES_LAUNCHED_KEY, default = 1) shouldEqual 0
        }
    }

    @Nested
    @DisplayName("when conditions are not satisfied")
    inner class ConditionsUnsatisfied {
        @BeforeEach
        fun setUp() {
            revue.dialogBuilder = mock()
            revue.init(context)
            storage.setTestValues(TIMES_LAUNCHED_KEY to DEFAULT_TIMES_LAUNCHED - 1)
        }

        @Test
        @DisplayName("request does not show a dialog")
        fun request() {
            revue.request(context)

            verify(revue.dialogBuilder, never()).build(context)
        }

        @Test
        @DisplayName("request returns false")
        fun requestReturnsFalse() {
            revue.request(context) shouldBe false
        }


        @Test
        @DisplayName("request does not reset times launched to zero")
        fun requestDoesNotReset() {
            revue.request(context)

            storage.getInt(TIMES_LAUNCHED_KEY, default = 0) shouldEqual DEFAULT_TIMES_LAUNCHED - 1
        }
    }
}
