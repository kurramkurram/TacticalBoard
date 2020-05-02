package io.github.kurramkurram.futaltacticalboard

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class Player(
    context: Context,
    id: Int,
    resourceId: Int,
    name: String?,
    wm: WindowManager,
    point: Array<Int>,
    gravity: Int
) : OnPlayerTouchListener.OnUpdateCallbackListener, PlayerAnimation.OnAnimationCallback {

    val mId = id
    private val mPlayer: View
    private val mIcon: ImageView
    var mName: TextView
    private var mWindowManager = wm
    val mParams: WindowManager.LayoutParams = getLayoutParams()
    private val mLayout: LinearLayout
    private val mAnimation = PlayerAnimation()
    private val mAnimationArray = ArrayList<Array<Int>>()

    init {
        val inflater = LayoutInflater.from(context)
        mPlayer = inflater.inflate(R.layout.player_layout, null)
        mLayout = mPlayer.findViewById(R.id.player_layout_view)
        val listener = OnPlayerTouchListener()
        listener.setCallbackListener(this)
        mPlayer.setOnTouchListener(listener)

        mAnimation.setListener(this)

        mIcon = mPlayer.findViewById(R.id.player_icon)
        mIcon.setImageResource(resourceId)
        mName = mPlayer.findViewById(R.id.player_name)
        mName.text = name

        mParams.x = point[0]
        mParams.y = point[1]
        mParams.gravity = gravity or Gravity.START
    }

    fun add() {
        mWindowManager.addView(mPlayer, mParams)
    }

    override fun update(deltaPoint: Array<Int>) {
        mParams.x += deltaPoint[0]
        if (mParams.gravity == Gravity.BOTTOM or Gravity.START) {
            mParams.y -= deltaPoint[1]
        } else {
            mParams.y += deltaPoint[1]
        }
        mWindowManager.updateViewLayout(mPlayer, mParams)
    }

    override fun updateAnimation(point: Array<Int>) {
        mParams.x = point[0]
        mParams.y = point[1]
        mWindowManager.updateViewLayout(mPlayer, mParams)
    }

    override fun endAnimation() {
        mAnimationArray.clear()
    }

    fun put(point: Array<Int>) {
        mAnimationArray.add(point)
    }

    fun play() {
        mAnimation.startAnimation(mAnimationArray)
    }

    private fun isAttachedToWindow() = mPlayer.isAttachedToWindow

    fun remove() {
        if (isAttachedToWindow()) {
            mWindowManager.removeView(mPlayer)
        }
    }

    fun setName(name: String?) {
        mName.text = name
    }

    fun setIcon(resourceId: Int) {
        mIcon.setImageResource(resourceId)
    }

    private fun getLayoutParams(): WindowManager.LayoutParams {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }
}