package io.github.kurramkurram.futaltacticalboard

import android.os.Handler
import android.util.Log
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

        next()
    }


    private fun next() {
        Log.d("PlayAnimation", "#next size = ${mAnimationArray.size} index = $mAnimationIndex")
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
        }
    }

    private fun nextFrame() {
        Log.d("PlayAnimation", "#nextFrame")
        mDistanceX -= mDiffX
        mDistanceY -= mDiffY
        if (0 >= mDistanceX || 0 >= mDistanceY) {
            mFromX = mToX
            mFromY = mToY
            mListener.updateAnimation(arrayOf(mFromX, mFromY))
            mHandler.removeCallbacks(mRunnable)
            next()
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
        mHandler.removeCallbacks(mRunnable)
        mHandler.postDelayed(mRunnable, 100)
    }

    fun setListener(listener: OnAnimationCallback) {
        mListener = listener
    }

    interface OnAnimationCallback {
        fun updateAnimation(point: Array<Int>)

        fun endAnimation()
    }
}