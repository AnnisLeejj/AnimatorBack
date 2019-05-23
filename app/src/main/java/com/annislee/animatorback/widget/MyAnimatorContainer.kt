package com.annislee.animatorback.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.RelativeLayout
import androidx.core.view.marginTop
import kotlinx.android.synthetic.main.part_animator_container.view.*

/**
 *@author Annis
 *@data 2019/5/23 14:27
 *@description
 */
class MyAnimatorContainer : RelativeLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private var mHeight: Int = 0
    private var starMarginTop: Int = 0
    var aivSkyLineHeight: Int = 0
    var aivMountainHeight: Int = 0
    var aivLandHeight: Int = 0
    private fun initView() {

    }
    private var mAivPlane: MyPlane? = null
    fun setPlane(aivPlane: MyPlane?) {
        mAivPlane = aivPlane
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        flickerStar()
    }

    var mPercent = 0f
    fun dragY(dY: Float) {
        mPercent = dY / mHeight
        if (mPercent < 0) {
            mPercent = 0f
        }
        if (mPercent > 1) {
            mPercent = 1f
        }
        resetHeightByPercent(mPercent)
    }

    private fun resetHeightByPercent(percent: Float) {
        mPercent = percent
        /**
         * 背景图
         */
        resetHeightByPercent(this, mHeight, percent)
        resetHeightByPercent(aivSkyLine, aivSkyLineHeight, percent)
        resetHeightByPercent(aivMountain, aivMountainHeight, percent)
        resetHeightByPercent(aivLand, aivLandHeight, percent)
        /**
         * 飞机斜度
         */
        mAivPlane?.setPercent(percent)
        /**
         * 星星高度
         */
        val starParams = aivStar.layoutParams
        (aivStar.layoutParams as? MarginLayoutParams)?.topMargin = (starMarginTop + starMarginTop * 2 * percent).toInt()
        aivStar.layoutParams = starParams
    }

    /**
     * 根据百分比设置高度
     */
    private fun resetHeightByPercent(aiv: View, height: Int, percent: Float) {
        val params = aiv.layoutParams
        params.height = (height + height * percent).toInt()
        aiv.layoutParams = params
    }

    var valueAnimator: ValueAnimator? = null
    fun release() {
        valueAnimator ?: run {
            valueAnimator = ValueAnimator.ofFloat(mPercent, 0f)
            valueAnimator?.interpolator = OvershootInterpolator()
            valueAnimator?.duration = 500
            valueAnimator?.addUpdateListener {
                val value = it.animatedValue as Float
                resetHeightByPercent(value)
                if (value == 0.0f) {
                    mAivPlane?.fly()
                }
//                Log.w("AnimatorContainer", "value: $value")
            }
        }
        valueAnimator?.cancel()
        valueAnimator?.setFloatValues(mPercent, 0f)

        valueAnimator?.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mHeight == 0)//初始化
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (starMarginTop == 0) {
            starMarginTop = aivStar.marginTop
        }
        if (aivSkyLineHeight == 0) {
            aivSkyLineHeight = aivSkyLine.measuredHeight
        }
        if (aivMountainHeight == 0) {
            aivMountainHeight = aivMountain.measuredHeight
        }
        if (aivLandHeight == 0) {
            aivLandHeight = aivLand.measuredHeight
        }
    }

    /**
     * 星星闪烁
     */
    private fun flickerStar() {
        // 第一种写法
        val animator1 = ObjectAnimator.ofFloat(aivStar, "scaleX", 1f, 1.5f, 0.8f, 1f)
        animator1.setDuration(2500).repeatCount = ValueAnimator.INFINITE
        animator1.start()

        val animator2 = ObjectAnimator.ofFloat(aivStar, "scaleY", 1f, 1.5f, 0.8f, 1f)
        animator2.setDuration(2500).repeatCount = ValueAnimator.INFINITE
        animator2.start()

        // 第二种写法
//        Path path = new Path();
//        path.moveTo(1,1);
//        path.lineTo(1.5f,1.5f);
//        path.lineTo(0.8f,0.8f);
//        path.lineTo(1.0f,1.0f);
//        ObjectAnimator oa = ObjectAnimator.ofFloat(mStarIV,"scaleX","scaleY",path).setDuration(2000);
//        oa.setRepeatCount(ValueAnimator.INFINITE);
//        oa.start();

        // 第三种写法
//        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mStarIV,"scaleX",1f,1.5f,0.8f,1f);
//        animator1.setDuration(2000).setRepeatCount(ValueAnimator.INFINITE);
//
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mStarIV,"scaleY",1f,1.5f,0.8f,1f);
//        animator2.setDuration(2000).setRepeatCount(ValueAnimator.INFINITE);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(animator1,animator2);
//        set.setDuration(2000);
//        set.start();

        // 第四种写法
//        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("scaleX",1f,1.5f,0.8f,1f);
//        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("scaleY",1f,1.5f,0.8f,1f);
//        ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(mStarIV,p1,p2)
//                .setDuration(2000);
//        oa.setRepeatCount(ValueAnimator.INFINITE);
//        oa.start();

        // 第五种写法
//        ScaleAnimation ta = new ScaleAnimation(1f,1.5f,1f,1.5f,
//                Animation.RELATIVE_TO_SELF,0.5f,
//                Animation.RELATIVE_TO_SELF,0.5f);

//        ScaleAnimation ta = new ScaleAnimation(1f,1.5f,1f,1.5f
//                );
//        ta.setRepeatCount(Animation.INFINITE);
//        ta.setDuration(2000);
//        mStarIV.setAnimation(ta);
//        ta.startNow();
    }
}