package com.annislee.animatorback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.annislee.animatorback.utils.DensityUtil

/**
 * @author Annis
 * @data 2019/5/23 17:10
 * @description 用于画路径
 */
@SuppressLint("DrawAllocation")
class PathView : View {
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPoint(pointStart!!, Color.RED, canvas)
        drawPoint(pointControlUp!!, Color.GREEN, canvas)
        drawPoint(pointControlDown!!, Color.GRAY, canvas)
        drawPoint(pointEnd!!, Color.BLUE, canvas)

        drawPath(pointStart!!, pointControlUp!!, pointEnd!!, canvas)
        drawPath(pointEnd!!, pointControlDown!!, pointStart!!, canvas)
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

    private fun drawPoint(point: Point, color: Int, canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = 3f

        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), 10f, paint)
    }


    private fun drawPath(start: Point, control: Point, end: Point, canvas: Canvas) {
        var path = Path()
        path.moveTo(start.x.toFloat(), start.y.toFloat())
        path.quadTo(control.x.toFloat(), control.y.toFloat(), end.x.toFloat(), end.y.toFloat())

        var paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.isDither = true
        paint.isAntiAlias = true

        canvas.drawPath(path, paint)
    }
}