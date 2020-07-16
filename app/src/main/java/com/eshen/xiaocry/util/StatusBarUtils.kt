package com.eshen.xiaocry.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.view.View
import android.view.WindowManager

/**
 * 状态栏相关工具类
 *
 */
object StatusBarUtils {

    /**
     * 设置状态栏颜色
     */
    fun setStatusColor(activity: Activity, isTranslate: Boolean, isDarkText: Boolean, @ColorRes bgColor: Int) {
        //如果系统为6.0及以上，就可以使用Android自带的方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) //可有可无
            decorView.systemUiVisibility =
                    (if (isTranslate) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else 0) or if (isDarkText) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
            window.statusBarColor =
                    if (isTranslate) Color.TRANSPARENT else ContextCompat.getColor(activity, bgColor) //Android5.0就可以用
        }
    }
}
