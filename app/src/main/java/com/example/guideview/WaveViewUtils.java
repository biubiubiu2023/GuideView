package com.example.guideview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 创建时间：2023/3/7
 * 创建人： 陈群
 * 功能描述：
 */
public class WaveViewUtils {
    public static final int DURATION = 900;
    public static final long ALLDURATION = DURATION * 5;

    public static Handler mHandler = new Handler(Looper.getMainLooper());

    private float mInitialRadius;   // 初始波纹半径
    private float mMaxRadius;   // 最大波纹半径
    private long mDuration = DURATION; // 一个波纹从创建到消失的持续时间
    private int mSpeed = DURATION;   // 波纹的创建速度，每500ms创建一个
    private boolean mIsRunning;
    private long mLastCreateTime;
    private List<Circle> mCircleList = new ArrayList<Circle>();
    private Interpolator mInterpolator = new LinearInterpolator();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private View mCurrentView ;

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                mHandler.postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    float circleRadius = 0f;
    float circleX = 0f;
    float circleY = 0f;

    public void initView(View view, float circleX, float circleY, float circleRadius) {
        mCurrentView = view;
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        this.circleX = circleX;
        this.circleY = circleY;
        this.circleRadius = circleRadius;
        mMaxRadius =  circleRadius * 2f;
        mInitialRadius = circleRadius;
    }


    public void onDraw(Canvas canvas) {
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(circleX, circleY, radius, mPaint);
            } else {
                iterator.remove();
            }
        }

    }
    public void invalid(){
        if(null != mCurrentView) {
            if (mCircleList.size() > 0) {
                mCurrentView.postInvalidateDelayed(16);
            }
        }
    }

    /**
     * 开始
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
        }
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        if(null != mCurrentView){
            mCurrentView.invalidate();
        }

        mHandler.removeCallbacks(mCreateCircle);
    }

    private void newCircle() {
        if(null == mCurrentView){
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        mCurrentView.invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }
}
