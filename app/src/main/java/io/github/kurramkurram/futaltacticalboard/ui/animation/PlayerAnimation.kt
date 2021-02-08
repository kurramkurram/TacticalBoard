package io.github.kurramkurram.futaltacticalboard.ui.animation

import android.os.Handler
import kotlin.math.abs

class PlayerAnimation {

    private var mToX = 0
    private var mToY = 0
    private var mFromX = 0
    private var mFromY = 0

    private var mDiffX = 0
    private var mDiffY = 0

    private var mDistanceX = 0
    private var mDistanceY = 0

    private lateinit var mListener: OnAnimationCallback
    private val mHandler = Handler()
    private lateinit var mAnimationArray: ArrayList<Array<Int>>
    private var mAnimationIndex = 1

    private val mRunnable = Runnable { nextFrame() }

    companion object {
        const val DISTANCE_WEIGHT = 10
    }

    fun startAnimation(toAnimationArray: ArrayList<Array<Int>>) {
        mAnimationArray = toAnimationArray

        // 初期位置に移動
        mFromX = mAnimationArray[0][0]
        mFromY = mAnimationArray[0][1]
        mListener.updateAnimation(arrayOf(mFromX, mFromY))
        mListener.next(mAnimationIndex)
    }

    fun next() {
        if (mAnimationArray.size - 1 >= mAnimationIndex) {

            mToX = mAnimationArray[mAnimationIndex][0]
            mToY = mAnimationArray[mAnimationIndex][1]

            mDistanceX = abs(mToX - mFromX)
            mDistanceY = abs(mToY - mFromY)

            mDiffX = mDistanceX / DISTANCE_WEIGHT
            mDiffY = mDistanceY / DISTANCE_WEIGHT

            mAnimationIndex++
            nextFrame()
        } else {
            mAnimationIndex = 1
            mListener.endAnimation()
            mHandler.removeCallbacks(mRunnable)
        }
    }

    private fun nextFrame() {
        mDistanceX -= mDiffX
        mDistanceY -= mDiffY

        if (0 >= mDistanceX || 0 >= mDistanceY) {
            mFromX = mToX
            mFromY = mToY
            mListener.updateAnimation(arrayOf(mFromX, mFromY))
            mListener.next(mAnimationIndex)
            return
        }

        if (mToX - mFromX > 0) {
            mFromX += mDiffX
        } else {
            mFromX -= mDiffX
        }

        if (mToY - mFromY > 0) {
            mFromY += mDiffY
        } else {
            mFromY -= mDiffY
        }

        mListener.updateAnimation(arrayOf(mFromX, mFromY))
        mHandler.postDelayed(mRunnable, 10)
    }

    fun setListener(listener: OnAnimationCallback) {
        mListener = listener
    }

    interface OnAnimationCallback {
        fun updateAnimation(point: Array<Int>)

        fun endAnimation()

        fun next(index: Int)
    }
}