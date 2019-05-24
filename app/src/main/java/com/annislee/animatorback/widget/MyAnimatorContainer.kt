package com.annislee.animatorback.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import kotlinx.android.synthetic.main.part_animator_container.view.*

/**
 *@author Annis
 *@data 2019/5/23 14:27
 *@description
 */
class MyAnimatorContainer : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private var mHeight: Int = 0
    var aivSkyLineHeight: Int = 0
    var aivMountainHeight: Int = 0
    var aivLandHeight: Int = 0
    private var starMarginTopMap: HashMap<AppCompatImageView, Int> = hashMapOf()
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
    private var letgoPercent = 0f //放开时的放大比
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
         * 树的百分比效果
         */
        aivTree?.setPercent(percent)
        /**
         * 星星高度
         */
        resetMarginTopByPercent(aivStar, percent)
        resetMarginTopByPercent(aivStar2, percent)
        resetMarginTopByPercent(aivStar3, percent)
        resetMarginTopByPercent(aivStar4, percent)
        resetMarginTopByPercent(aivStar5, percent)
    }

    /**
     * 根据百分比设置高度
     */
    private fun resetMarginTopByPercent(aiv: AppCompatImageView, percent: Float) {
        val starParams = aiv.layoutParams
        val marginTop = starMarginTopMap[aiv] ?: 0
        val top = marginTop + marginTop * 2 * percent
        (aiv.layoutParams as? MarginLayoutParams)?.topMargin = top.toInt()
        aiv.layoutParams = starParams
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
        if (mPercent == 0f) {
            return//被还到原位时 不执行动画
        }
        letgoPercent = mPercent
        valueAnimator ?: run {
            valueAnimator = ValueAnimator.ofFloat(mPercent, 0f)
            valueAnimator?.interpolator = OvershootInterpolator()
            valueAnimator?.duration = 500
            valueAnimator?.addUpdateListener {
                val value = it.animatedValue as Float
                resetHeightByPercent(value)
                if (value == 0.00f) {
                    mAivPlane?.fly(letgoPercent)
                }
            }
        }
        valueAnimator?.cancel()
        valueAnimator?.setFloatValues(mPercent, 0f)

        valueAnimator?.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mHeight == 0) {//初始化
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
            mAivPlane?.init(MeasureSpec.getSize(widthMeasureSpec), mHeight)
        }
        if (starMarginTopMap.isEmpty()) {
            starMarginTopMap[aivStar] = aivStar.marginTop
            starMarginTopMap[aivStar2] = aivStar2.marginTop
            starMarginTopMap[aivStar3] = aivStar3.marginTop
            starMarginTopMap[aivStar4] = aivStar4.marginTop
            starMarginTopMap[aivStar5] = aivStar5.marginTop
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

    private fun flickerStar() {
        flickerStar(aivStar)
        flickerStar(aivStar2)
        flickerStar(aivStar3)
        flickerStar(aivStar4)
        flickerStar(aivStar5)
    }

    /**
     * 星星闪烁
     */
    private fun flickerStar(aivStar: AppCompatImageView) {
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