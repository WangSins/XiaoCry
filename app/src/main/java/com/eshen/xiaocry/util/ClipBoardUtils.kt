package com.eshen.xiaocry.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipBoardUtils {

    /**
     * 复制到裁剪版
     */
    fun copyClipBoard(context: Context, label: String, text: String) {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
            primaryClip = ClipData.newPlainText(label, text)
        }
    }
}