package com.annislee.animatorback.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.annislee.animatorback.utils.BezierUtil
import com.annislee.animatorback.utils.DensityUtil

/**
 * @author Annis
 * @data 2019/5/23 16:27
 * @description  飞机
 */
@SuppressLint("DrawAllocation")
class MyPlane : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var pointStart: PointF? = null
    private var pointControlUp: PointF? = null
    private var pointControlDown: PointF? = null
    private var pointEnd: PointF? = null
    private var trackStart: PointF? = null
    fun init(pWidth: Int, pHeight: Int) {
        DensityUtil.dip2px(context, 20f).apply {
            pointEnd = PointF((this + pWidth).toFloat(), this.toFloat())
        }
        pointControlUp = PointF((pWidth * 0.8).toFloat(), (pHeight * 0.95).toFloat())
        pointControlDown = PointF((pWidth * -0.5).toFloat(), (pHeight * 0.8).toFloat())
        initAnimator()
    }

    var layoutDX: Int = 0
    var layoutDY: Int = 0
    var layoutCX: Int = 0
    var layoutCY: Int = 0
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (layoutDX == 0 || layoutDY == 0) {
            layoutDX = right - left
            layoutDY = bottom - top
            //中心点
            layoutCX = (right + left) / 2
            layoutCY = (bottom + top) / 2
            pointStart = PointF(layoutCX.toFloat(), layoutCY.toFloat())
            trackStart = PointF(-layoutCX.toFloat(), layoutCY.toFloat())
            initAnimator()
        }
    }

    private fun initAnimator() {
        initUpAnimation()
        initDownAnimation()
        initLevelAnimation()
    }

    /**
     * 飞翔
     */
    private var animatorSet: AnimatorSet? = null

    fun fly(percent: Float) {
        Log.w("Animator MyPlane", "我要飞了")
        if (animatorSet == null) {
            animatorSet = AnimatorSet()
        }
        if (animatorSet?.isRunning != true) {
            downAnimator?.duration = (downDuration * (1 - durationChangeRange * percent)).toLong()
            levelAnimator?.duration = (levelDuration * (1 - durationChangeRange * percent)).toLong()
            upAnimator?.duration = (upDuration * (1 - durationChangeRange * percent)).toLong()
            animatorSet?.play(downAnimator)?.before(levelAnimator)?.after(upAnimator)
            animatorSet?.start()
        }
    }

    /**
     * 动画百分比
     */
    fun setPercent(percent: Float) {
        if (animatorSet?.isRunning == true) {
            animatorSet?.cancel()
        }
        rotation = -45 * percent
    }

    private var upAnimator: ValueAnimator? = null
    private var downAnimator: ValueAnimator? = null
    private var levelAnimator: ValueAnimator? = null

    private val upDuration = 1500L
    private val downDuration = 2000L
    private val levelDuration = 500L
    private val durationChangeRange = 0.6f

    /**
     * 下降动画
     */
    private fun initDownAnimation() {
        if (pointStart == null) return
        val typeEvaluator = MyTypeEvaluator(pointControlDown!!)
        downAnimator = ValueAnimator.ofObject(typeEvaluator, pointEnd, trackStart)
        downAnimator?.duration = downDuration
        downAnimator?.interpolator = AccelerateInterpolator()
        downAnimator?.addUpdateListener {
            var currentPointF = it.animatedValue as PointF
            setLayout(currentPointF)
        }
    }

    /**
     * 上升动画
     */
    private fun initUpAnimation() {
        if (pointStart == null) return
        val typeEvaluator = MyTypeEvaluator(pointControlUp!!)
        upAnimator = ValueAnimator.ofObject(typeEvaluator, pointStart, pointEnd)
        upAnimator?.duration = upDuration
        upAnimator?.interpolator = DecelerateInterpolator()
        upAnimator?.addUpdateListener {
            var currentPointF = it.animatedValue as PointF
            setLayout(currentPointF)
        }
    }

    /**
     * 水平动画
     */
    private fun initLevelAnimation() {
        if (pointStart == null) return
        levelAnimator = ValueAnimator.ofObject(TypeEvaluator<PointF> { fraction, startValue, endValue ->
            return@TypeEvaluator PointF(startValue.x + fraction * (endValue.x - startValue.x), startValue.y)
        }, trackStart, pointStart)
        levelAnimator?.duration = levelDuration
        levelAnimator?.interpolator = DecelerateInterpolator()
        levelAnimator?.addUpdateListener {
            var currentPointF = it.animatedValue as PointF
            setLayout(currentPointF)
        }
    }

    var lastPointF: PointF = PointF(0f, 0f)
    private fun setLayout(piontf: PointF) {
        Log.w("Animator MyPlane", "我的位置:${piontf.x} - ${piontf.y}")
        //位置
        layout(
            piontf.x.toInt() - layoutDX / 2,
            piontf.y.toInt() - layoutDY / 2,
            (piontf.x + layoutDX / 2).toInt(),
            (piontf.y + layoutDY / 2).toInt()
        )
        //斜率
        val angle = Math.atan2(
            (piontf.y - lastPointF.y).toDouble(),
            (piontf.x - lastPointF.x).toDouble()
        ).toFloat()
        lastPointF = piontf

        rotation = (angle * 180 / Math.PI).toFloat()
    }

    /**
     * PointF 估值器
     */
    class MyTypeEvaluator(private val controlValue: PointF) : TypeEvaluator<PointF> {
        override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF =
            BezierUtil.getPointFromQuadBezier(fraction, startValue, controlValue, endValue)
    }
}