package com.example.guideview

import android.animation.Animator
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SizeUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 创建时间：2022/11/24
 * 创建人： 陈群
 * 功能描述：kotlin 扩展方法 工具类
 */


/**
 * 隐藏显示 动画
 */
fun View.startAlphaAnim(show:Boolean, isVisibleOrGone:Boolean = true, onEnd: (() -> Unit)? =null){
    val toAlpha = if(show){
        this.alpha = 0f
        1f
    }else{
        this.alpha = 1f
        0f
    }
    this.animate().alpha(toAlpha).setDuration(300).setListener(object : Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator) {
            if(isVisibleOrGone && show){
                alpha = 0f
                visibility = View.VISIBLE
            }
        }
        override fun onAnimationEnd(animation: Animator) {
            if(isVisibleOrGone && !show){
                visibility = View.GONE
            }
            if (onEnd != null) {
                onEnd()
            }
        }
        override fun onAnimationCancel(animation: Animator) {
            if(isVisibleOrGone && !show){
                visibility = View.GONE
            }
            if (onEnd != null) {
                onEnd()
            }
        }

        override fun onAnimationRepeat(animation: Animator) {
        }

    }).start()
}


