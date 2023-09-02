package com.example.guideview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils

/**
 * 创建时间：2023/3/6
 * 创建人： 陈群
 * 功能描述：
 */
class GuideBackgroundView1:View {
    constructor(context: Context) : super(context){
        initView(context,null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initView(context,attrs)
    }
    lateinit var mBackgroundPaint:Paint
    lateinit var transPaint:Paint


    var cWidth = 0f
    var cHeight = 0f

    var startPathY = 0f
    var endPathY = 0f
    var quadX = 0f
    var quadY = 0f

    var circleRadius = 0f
    var circleX = 0f
    var circleY = 0f

    var guideType = 0

    var outLineWidth = 0f

    lateinit var mRectF:RectF

    var mBackgroundColor:Int =0

    var waveViewUtils: WaveViewUtils? = null

    private fun initView(context: Context, attrs: AttributeSet?) {
        waveViewUtils = WaveViewUtils()


        mBackgroundColor = ContextCompat.getColor(context,R.color.black_trans_60)

        mBackgroundPaint = Paint()
        mBackgroundPaint.color = ContextCompat.getColor(context, R.color.color_ff5f17)
        mBackgroundPaint.style = Paint.Style.FILL
        mBackgroundPaint.isAntiAlias = true

        transPaint = Paint()
        transPaint.color = Color.parseColor("#00000000")
        transPaint.style = Paint.Style.FILL
        transPaint.isAntiAlias = true
        transPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        outLineWidth = SizeUtils.dp2px(20f).toFloat()
//        transPaintOut = Paint()
//        transPaintOut.color = Color.parseColor("#33ffffff")
//        transPaintOut.style = Paint.Style.STROKE
//        transPaintOut.strokeWidth = outLineWidth
//        transPaintOut.isAntiAlias = true


        val a: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MainGuideViewStyle
        )
        guideType = a.getInt(R.styleable.MainGuideViewStyle_guideType, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        cWidth = measuredWidth.toFloat()
        cHeight = measuredHeight.toFloat()
        circleRadius = SizeUtils.dp2px(35f).toFloat()

        when(guideType){
            3 -> {
                startPathY = measuredHeight / 2f
                endPathY = measuredHeight * 3f/4f
                quadX = measuredWidth * 7f/20f
                quadY = measuredHeight * 9f/20f

                circleX = measuredWidth.toFloat() * 8.75f/10f
                circleY = measuredHeight.toFloat() - circleRadius
            }
            2 -> {
                startPathY = measuredHeight / 2f
                endPathY = measuredHeight * 13f/20f
                quadX = measuredWidth * 7f/20f
                quadY = measuredHeight * 9f/20f

                circleX = measuredWidth.toFloat() * 6.25f / 10f
                circleY = measuredHeight.toFloat() - circleRadius

            }
            1 -> {
                startPathY = measuredHeight * 13f/20f
                endPathY = measuredHeight / 2f
                quadX = measuredWidth * 11f/20f
                quadY = measuredHeight * 9f/20f

                circleX = measuredWidth.toFloat() * 3.75f / 10f
                circleY = measuredHeight.toFloat() - circleRadius
            }
            0 -> {
                startPathY = measuredHeight * 3f/4f
                endPathY = measuredHeight / 2f
                quadX = measuredWidth * 13f/20f
                quadY = measuredHeight * 9f/20f

                circleX = measuredWidth.toFloat() * 1.25f / 10f
                circleY = measuredHeight.toFloat() - circleRadius
            }

        }

        mRectF = RectF(circleX-circleRadius-outLineWidth/2f,circleY-circleRadius-outLineWidth/2f,circleX+circleRadius+outLineWidth/2f,circleY+circleRadius+outLineWidth/2f)

        waveViewUtils?.initView(this,circleX,circleY,circleRadius)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val i1 = canvas.saveLayer(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            null,
            Canvas.ALL_SAVE_FLAG
        )
        canvas.drawColor(mBackgroundColor)
        val path = Path()
        path.moveTo(cWidth, startPathY)
        path.lineTo(cWidth, cHeight)
        path.lineTo(0f, cHeight)
        path.lineTo(0f, endPathY)
        path.quadTo(quadX,quadY,cWidth, startPathY)
        path.close()
        canvas.drawPath(path, mBackgroundPaint)

        waveViewUtils?.onDraw(canvas)

        canvas.drawCircle(circleX,circleY,circleRadius,transPaint)
        canvas.restoreToCount(i1)

//        canvas.drawArc(mRectF,360f,360f,false,transPaintOut)

        waveViewUtils?.invalid()
    }

    fun stopAnim() {
        waveViewUtils?.stopImmediately()
    }

    fun startAnim() {
        waveViewUtils?.start()
    }

}