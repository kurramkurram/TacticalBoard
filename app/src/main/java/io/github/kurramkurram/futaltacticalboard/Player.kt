package io.github.kurramkurram.futaltacticalboard

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

class Player(
    context: Context,
    resourceId: Int,
    name: String,
    wm: WindowManager,
    point: Array<Int>,
    gravity: Int
) : OnPlayerTouchListener.OnUpdateCallbackListener {

    private var mPlayer: View
    private var mIcon: ImageView
    private var mName: TextView
    private var mWindowManager = wm
    private var mParams: WindowManager.LayoutParams = getLayoutParams()

    init {
        val inflater = LayoutInflater.from(context)
        mPlayer = inflater.inflate(R.layout.player_layout, null)
        val listener = OnPlayerTouchListener()
        listener.setCallbackListener(this)
        mPlayer.setOnTouchListener(listener)
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

    private fun isAttachedToWindow() = mPlayer.isAttachedToWindow

    fun remove() {
        if (isAttachedToWindow()) {
            mWindowManager.removeView(mPlayer)
        }
    }

    fun setName(name: String) {
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