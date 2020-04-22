package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import io.github.kurramkurram.futaltacticalboard.OnLineTouchListener

class DrawLine(context: Context?) : View(context),
    OnLineTouchListener.OnInvalidateCallbackListener {

    private val mPathList = ArrayList<Path>()
    private val mPaint = Paint()

    init {
        val listener = OnLineTouchListener()
        listener.setCallbackListener(this)
        listener.setPathList(mPathList)
        setOnTouchListener(listener)

        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 10F
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (path in mPathList) {
            canvas!!.drawPath(path, mPaint)
        }
    }

    override fun invalidateLine() {
        invalidate()
    }
}