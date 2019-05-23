package com.annislee.animatorback

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.part_animator_container.*

class AnimatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_animator)

        listener()
    }

    var rawDownY: Float = 0f
    private fun listener() {
        animatorContainer.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    rawDownY = event.rawY
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    val rawY = event.rawY
                    if (rawY > rawDownY) {
                        val dY = rawY - rawDownY
                        animatorContainer.dragY(dY)
                    }
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    animatorContainer.release()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
        animatorContainer.setPlane(aivPlane)
    }
}