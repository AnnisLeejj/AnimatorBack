package com.annislee.animatorback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.annislee.animatorback.utils.DensityUtil

/**
 * @author Annis
 * @data 2019/5/23 16:27
 * @description
 */
@SuppressLint("DrawAllocation")
class MyPlane : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {

    }
    /**
     * 动画百分比
     */
    fun setPercent(percent: Float) {
        rotation = -45 * percent
    }

    var pointStart: Point? = null
    private var pointControlUp: Point? = null
    private var pointControlDown: Point? = null
    var pointEnd: Point? = null
    private var mHeight = 0
    var mWidth = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        Log.w("Animator PathView", "$mWidth - $mHeight")

        pointStart = Point(DensityUtil.dip2px(context, 30f), mHeight)
        pointEnd = Point(mWidth, DensityUtil.dip2px(context, 30f))
        pointControlUp = Point((mWidth * 0.8).toInt(), (mHeight * 0.95).toInt())
        pointControlDown = Point((mWidth * -0.5).toInt(), (mHeight * 0.8).toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    /**
     * 飞翔
     */
    fun fly() {
        Log.w("Animator MyPlane", "我要飞了")
    }
}
