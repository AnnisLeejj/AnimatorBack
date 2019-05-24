package com.annislee.animatorback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author Annis
 * @data 2019/5/23 16:27
 * @description  飞机
 */
@SuppressLint("DrawAllocation")
class MyTree : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 动画百分比
     */
    fun setPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
//        if (animatorSet?.isRunning == true) {
//            animatorSet?.cancel()
//        }
//        rotation = -45 * percent
    }
}