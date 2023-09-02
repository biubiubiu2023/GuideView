package com.example.guideview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.guideview.databinding.ViewMainGuide1Binding
import com.example.guideview.databinding.ViewMainGuide2Binding
import com.example.guideview.databinding.ViewMainGuide3Binding
import com.example.guideview.databinding.ViewMainGuide4Binding


/**
 * 创建时间：2023/3/6
 * 创建人： 陈群
 * 功能描述：
 */
class MainGuideView: FrameLayout {
    constructor(context: Context) : super(context){
        initView(context,null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initView(context,attrs)
    }
    lateinit var skipButton:TextView
    private fun initView(context: Context, attrs: AttributeSet?) {
        removeAllViews()

        val layoup = LayoutParams(LayoutParams.WRAP_CONTENT,SizeUtils.dp2px(30f))
        layoup.gravity = Gravity.END
        layoup.setMargins(SizeUtils.dp2px(20f),SizeUtils.dp2px(20f)+BarUtils.getStatusBarHeight(),SizeUtils.dp2px(20f),0)
        skipButton = TextView(context)
        skipButton.setPadding(SizeUtils.dp2px(10f),SizeUtils.dp2px(2f),SizeUtils.dp2px(10f),SizeUtils.dp2px(2f))
        skipButton.layoutParams = layoup
        skipButton.text = "跳过引导"
        skipButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12f)
        skipButton.setTextColor(ContextCompat.getColor(context,R.color.white))
        skipButton.gravity = Gravity.CENTER

        val bg = GradientDrawable()
        bg.shape = GradientDrawable.RECTANGLE
        bg.setColor(ContextCompat.getColor(context,R.color.color_ff5_30))
        bg.cornerRadius = SizeUtils.dp2px(20f).toFloat()
        bg.setStroke(SizeUtils.dp2px(1f),ContextCompat.getColor(context, R.color.color_ff5f17))
        skipButton.background = bg

        addView(skipButton,layoup)
        skipButton.setOnClickListener {
            index = guideSize
            doNextStep()
        }
    }

    val guideSize = 4
    var index = 1
    var mRootView:View? = null
    fun startGuideView() {
        if(null != mRootView){
            removeView(mRootView)
            mRootView = null
        }
        val binding = when(index){
            1 -> {
                ViewMainGuide1Binding.inflate(LayoutInflater.from(context))
            }
            2 -> {
                ViewMainGuide2Binding.inflate(LayoutInflater.from(context))
            }
            3 -> {
                ViewMainGuide3Binding.inflate(LayoutInflater.from(context))
            }
            4 -> {
                ViewMainGuide4Binding.inflate(LayoutInflater.from(context))
            }

            else -> {ViewMainGuide4Binding.inflate(LayoutInflater.from(context))}
        }
        mRootView = binding.root
        mRootView?.layoutDirection = LAYOUT_DIRECTION_LTR
        mRootView?.visibility = View.GONE
        addView(mRootView)
        mRootView?.startAlphaAnim(true)
        mRootView?.findViewById<GuideBackgroundView1>(R.id.iv_guide)?.startAnim()
        mRootView?.setOnClickListener {
           doNextStep()
        }
        if(index<=guideSize){
            WaveViewUtils.mHandler.postDelayed(mRun, WaveViewUtils.ALLDURATION)
        }
        skipButton.bringToFront()
    }
    val mRun = Runnable {
        doNextStep()
    }

    private fun doNextStep() {
        if(ActivityUtils.isActivityAlive(context)){
            WaveViewUtils.mHandler.removeCallbacks(mRun)
            mRootView?.setOnClickListener(null)
            mRootView?.findViewById<GuideBackgroundView1>(R.id.iv_guide)?.stopAnim()
            index++
            if(index>guideSize) {
                mRootView = null
                startAlphaAnim(false,isVisibleOrGone = true)
                return
            }
            mRootView?.startAlphaAnim(false,isVisibleOrGone = true){
                startGuideView()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        WaveViewUtils.mHandler.removeCallbacks(mRun)
    }
}