package com.eshen.xiaocry.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipBoardUtils {

    /**
     * 复制到裁剪版
     */
    fun copyClipBoard(context: Context, label: String, text: String) {
        with(context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager) {
            setPrimaryClip(ClipData.newPlainText(label, text))
        }
    }
}