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

import android.content.DialogInterface
import com.github.dmchoull.revue.R

class PrePromptDialogBuilder(
        callback: DialogResultCallback? = null,
        title: String? = null,
        titleRes: Int = R.string.default_prompt_title,
        message: String? = null,
        messageRes: Int = R.string.default_prompt_message,
        positiveButton: String? = null,
        positiveButtonRes: Int = R.string.default_prompt_positive,
        positiveButtonListener: DialogInterface.OnClickListener? = null,
        negativeButton: String? = null,
        negativeButtonRes: Int = R.string.default_prompt_negative,
        negativeButtonListener: DialogInterface.OnClickListener? = null,
        dismissListener: DialogInterface.OnDismissListener? = null
) : SimpleDialogBuilder(
        callback = callback,
        title = title,
        titleRes = titleRes,
        message = message,
        messageRes = messageRes,
        positiveButton = positiveButton,
        positiveButtonRes = positiveButtonRes,
        positiveButtonListener = positiveButtonListener,
        negativeButton = negativeButton,
        negativeButtonRes = negativeButtonRes,
        negativeButtonListener = negativeButtonListener,
        dismissListener = dismissListener,
        withoutNegativeButton = false,
        withoutNeutralButton = true
)
