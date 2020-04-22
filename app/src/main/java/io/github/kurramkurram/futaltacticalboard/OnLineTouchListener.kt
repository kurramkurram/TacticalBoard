package io.github.kurramkurram.futaltacticalboard

import android.graphics.Path
import android.view.MotionEvent
import android.view.View

class OnLineTouchListener : View.OnTouchListener {

    private lateinit var mListener: OnInvalidateCallbackListener
    private lateinit var mPath: Path
    private lateinit var mPathList: ArrayList<Path>

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath = Path()
                mPath.moveTo(event.x, event.y)
                mPathList.add(mPath)
            }
            MotionEvent.ACTION_MOVE -> mPath.lineTo(event.x, event.y)

            MotionEvent.ACTION_UP -> mPath.lineTo(event.x, event.y)
        }

        mListener.invalidateLine()
        return true
    }

    fun setPathList(pathList: ArrayList<Path>) {
        mPathList = pathList
    }

    fun setCallbackListener(listener: OnInvalidateCallbackListener) {
        mListener = listener
    }

    interface OnInvalidateCallbackListener {
        fun invalidateLine()
    }

}