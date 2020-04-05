package io.github.kurramkurram.futaltacticalboard

import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class OnPlayerTouchListener : View.OnTouchListener {

    private lateinit var mListener: OnUpdateCallbackListener
    private var mLastX = 0
    private var mLastY = 0

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.rawX.toInt()
                mLastY = event.rawY.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1) {
                    val x = event.rawX.toInt()
                    val y = event.rawY.toInt()
                    val deltaX = x - mLastX
                    val deltaY = y - mLastY
                    mLastX = x
                    mLastY = y
                    if (abs(deltaX) >= 5 || abs(deltaY) >= 5) {
                        mListener.update(arrayOf(deltaX, deltaY))
                    }
                }
            }
        }

        return false
    }

    fun setCallbackListener(listener: OnUpdateCallbackListener) {
        mListener = listener
    }

    interface OnUpdateCallbackListener {
        fun update(deltaPoint: Array<Int>)
    }
}