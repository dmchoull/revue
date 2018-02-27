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
import com.github.dmchoull.revue.builder.DialogResult
import com.github.dmchoull.revue.builder.DialogResultCallback
import com.github.dmchoull.revue.builder.RevueDialogBuilder
import com.github.dmchoull.revue.builder.SimpleDialogBuilder
import com.github.dmchoull.revue.dialog.RevueDialog
import com.github.dmchoull.revue.util.StoreService
import com.nhaarman.mockitokotlin2.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RevueTest {
    private lateinit var context: Context
    private lateinit var reviewDialog: RevueDialog
    private lateinit var revue: Revue
    private lateinit var storage: InMemoryStorage
    private lateinit var storeService: StoreService

    @BeforeEach
    fun setUp() {
        context = mock()
        reviewDialog = mock()
        storage = InMemoryStorage()
        storeService = mock()

        revue = Revue(storage, storeService)
        revue.prePromptDialogBuilder = null
    }

    @Test
    @DisplayName("getInstance returns a singleton instance")
    fun getInstance() {
        Revue.instance shouldBeInstanceOf Revue::class
        Revue.instance shouldBe Revue.instance
    }

    @Test
    @DisplayName("uses SimpleDialogBuilder by default")
    fun defaultSimpleDialogBuilder() {
        revue.reviewPromptDialogBuilder shouldBeInstanceOf SimpleDialogBuilder::class
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

    @Test
    @DisplayName("init throws an exception if called twice on the same instance")
    fun initThrowsOnSecondCall() {
        revue.init(context)

        assertThrows(RevueAlreadyInitializedException::class.java) {
            revue.init(context)
        }
    }

    @Nested
    @DisplayName("showNow")
    inner class ShowNow {
        @BeforeEach
        fun setUp() {
            revue.reviewPromptDialogBuilder = mockDialogBuilder()
            revue.init(context)
        }

        @Nested
        @DisplayName("with pre-review prompt dialog")
        inner class WithPrePrompt {
            private lateinit var preReviewDialog: RevueDialog

            @BeforeEach
            fun setUp() {
                preReviewDialog = mock()
                revue.prePromptDialogBuilder = mockDialogBuilder(preReviewDialog)
            }

            @Test
            @DisplayName("shows review prompt dialog if user clicks positive button")
            fun afterPositive() {
                revue.showNow(context)

                argumentCaptor<DialogResultCallback>().apply {
                    verify(revue.prePromptDialogBuilder)!!.callback(capture())
                    verify(preReviewDialog).show()

                    firstValue.invoke(DialogResult.POSITIVE)

                    verify(reviewDialog).show()
                }
            }

            @Test
            @DisplayName("does not show review prompt dialog if user clicks negative button")
            fun afterNegative() {
                revue.showNow(context)

                argumentCaptor<DialogResultCallback>().apply {
                    verify(revue.prePromptDialogBuilder)!!.callback(capture())
                    verify(preReviewDialog).show()

                    firstValue.invoke(DialogResult.NEGATIVE)

                    verify(reviewDialog, never()).show()
                }
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
        @DisplayName("without pre-review prompt dialog")
        inner class WithoutPrePrompt {
            @BeforeEach
            fun setUp() {
                revue.prePromptDialogBuilder = null
            }

            @Test
            @DisplayName("shows review prompt dialog immediately")
            fun afterPositive() {
                revue.showNow(context)
                verify(reviewDialog).show()
            }

            @Test
            @DisplayName("resets times launched to zero")
            fun showNowReset() {
                storage.setTestValues(TIMES_LAUNCHED_KEY to 3)

                revue.showNow(context)

                storage.getInt(TIMES_LAUNCHED_KEY, default = 1) shouldEqual 0
            }
        }
    }

    private fun mockDialogBuilder(dialog: RevueDialog = reviewDialog) = mock<RevueDialogBuilder> {
        on { callback(any()) } doReturn it
        on { build(context) } doReturn dialog
    }

    @Nested
    @DisplayName("when conditions are satisfied")
    inner class ConditionsSatisfied {
        @BeforeEach
        fun setUp() {
            revue.reviewPromptDialogBuilder = mockDialogBuilder()
            revue.init(context)
            storage.setTestValues(TIMES_LAUNCHED_KEY to DEFAULT_TIMES_LAUNCHED)
        }

        @Test
        @DisplayName("request shows a dialog")
        fun request() {
            revue.request(context)

            verify(reviewDialog).show()
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
            revue.reviewPromptDialogBuilder = mockDialogBuilder()
            revue.init(context)
            storage.setTestValues(TIMES_LAUNCHED_KEY to DEFAULT_TIMES_LAUNCHED - 1)
        }

        @Test
        @DisplayName("request does not show a dialog")
        fun request() {
            revue.request(context)

            verify(revue.reviewPromptDialogBuilder, never()).build(context)
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

    @Nested
    @DisplayName("when dialog result callback is called")
    inner class Callback {
        @BeforeEach
        fun setUp() {
            revue.reviewPromptDialogBuilder = mockDialogBuilder()
            revue.init(context)
        }

        @Test
        @DisplayName("with a positive result it disables future dialogs")
        fun positiveResultDisables() {
            argumentCaptor<DialogResultCallback>().apply {
                revue.showNow(context)

                verify(revue.reviewPromptDialogBuilder).callback(capture())

                firstValue(DialogResult.POSITIVE)

                storage.getInt(ENABLED_KEY, default = 1) shouldEqual 0
            }
        }

        @Test
        @DisplayName("with a positive result it opens the store page for the app")
        fun positiveResultOpensStore() {
            argumentCaptor<DialogResultCallback>().apply {
                revue.showNow(context)

                verify(revue.reviewPromptDialogBuilder).callback(capture())

                firstValue(DialogResult.POSITIVE)

                storeService.openAppInStore(context)
            }
        }

        @Test
        @DisplayName("with a negative result it disables future dialogs")
        fun negativeResult() {
            argumentCaptor<DialogResultCallback>().apply {
                revue.showNow(context)

                verify(revue.reviewPromptDialogBuilder).callback(capture())

                firstValue(DialogResult.NEGATIVE)

                storage.getInt(ENABLED_KEY, default = 1) shouldEqual 0
            }
        }

        @Test
        @DisplayName("with a neutral result it does not disable future dialogs")
        fun neutralResult() {
            argumentCaptor<DialogResultCallback>().apply {
                revue.showNow(context)

                verify(revue.reviewPromptDialogBuilder).callback(capture())

                firstValue(DialogResult.NEUTRAL)

                storage.getInt(ENABLED_KEY, default = 1) shouldEqual 1
            }
        }
    }

    @Nested
    @DisplayName("when dialog is disabled")
    inner class Disabled {
        @BeforeEach
        fun setUp() {
            revue.reviewPromptDialogBuilder = mockDialogBuilder()
            revue.init(context)
            storage.setTestValues(ENABLED_KEY to 0, TIMES_LAUNCHED_KEY to DEFAULT_TIMES_LAUNCHED)
        }

        @Test
        @DisplayName("showNow does not show dialog")
        fun showNow() {
            revue.showNow(context)

            verify(revue.reviewPromptDialogBuilder, never()).build(context)
        }

        @Test
        @DisplayName("request does not show dialog")
        fun request() {
            revue.request(context)

            verify(revue.reviewPromptDialogBuilder, never()).build(context)
        }
    }
}
