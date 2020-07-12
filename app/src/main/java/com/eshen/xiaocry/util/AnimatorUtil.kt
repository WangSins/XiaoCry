package com.eshen.xiaocry.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by Sin on 2020/7/12
 */
object AnimatorUtil {

    private const val translationX:String = "translationX"
    private const val translationY:String = "translationY"
    private const val scaleX:String = "scaleX"
    private const val scaleY:String = "scaleY"
    private const val rotationX:String = "rotationX"
    private const val rotationY:String = "rotationY"
    private const val alpha:String = "alpha"

    fun scaleObjectAnimation(
        targetView: View,
        startSize: Float,
        endSize: Float,
        duration: Long
    ) {
        val scaleX = ObjectAnimator.ofFloat(targetView, scaleX, startSize, endSize)
        val scaleY = ObjectAnimator.ofFloat(targetView, scaleY, startSize, endSize)

        scaleX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (startSize > endSize) targetView.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationStart(animation: Animator?) {
                targetView.visibility = View.VISIBLE
            }
        })

        val set = AnimatorSet()
        set.play(scaleY).with(scaleX)
        set.duration = duration
        set.start()
    }
}