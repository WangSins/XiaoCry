package com.eshen.xiaocry.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

object ToastUtils {

    private var toast: Toast? = null

    /**
     * 展示Toast
     */
    @SuppressLint("ShowToast")
    fun showToast(context: Context, msg: String) {
        toast = if (toast == null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast?.cancel()
            Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        toast?.show()
    }
}