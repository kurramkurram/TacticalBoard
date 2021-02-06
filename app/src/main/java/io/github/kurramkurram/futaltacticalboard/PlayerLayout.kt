package io.github.kurramkurram.futaltacticalboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout

class PlayerLayout : LinearLayout, OnPlayerTouchListener.OnUpdateCallbackListener,
    PlayerAnimation.OnAnimationCallback {

    companion object {
        const val TAG = "Player1"
    }

    lateinit var mName: TextView
    var mId = 0
    var mAnimationArray = ArrayList<Array<Int>>()
    private lateinit var mCallback: OnAnimationCallback
    private val mAnimation = PlayerAnimation()
    private lateinit var mColorEnum: ColorEnum

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    @SuppressLint("ClickableViewAccessibility")
    constructor(
        context: Context,
        id: Int,
        resourceId: Int,
        colorEnum: ColorEnum,
        name: String,
        initX: Int,
        initY: Int,
        callback: OnAnimationCallback
    ) : super(context) {
        View.inflate(context, R.layout.player_layout, this)
        mId = id

        findViewById<ImageView>(R.id.player_icon).setImageDrawable(context.getDrawable(resourceId))

        mColorEnum = colorEnum

        mName = findViewById(R.id.player_name)
        mName.text = name

        x = initX.toFloat()
        y = initY.toFloat()

        mCallback = callback
        mAnimation.setListener(this)

        val listener = OnPlayerTouchListener()
        listener.setCallbackListener(this)
        this.setOnTouchListener(listener)
        setOnTouchListener(listener)
    }

    fun add(layout: ConstraintLayout) {
        layout.addView(this)
    }

    fun setName(name: String) {
        mName.text = name
    }

    fun putPoint(point: Array<Int>) {
        mAnimationArray.add(point)
    }

    fun play() {
        mAnimation.startAnimation(mAnimationArray)
    }

    fun next() {
        mAnimation.next()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    override fun update(deltaPoint: Array<Int>) {
        val dx = left + deltaPoint[0]
        val dy = top + deltaPoint[1]
        layout(dx, dy, dx + width, dy + height)
    }

    override fun updateAnimation(point: Array<Int>) {
        x = point[0].toFloat()
        y = point[1].toFloat()
    }

    override fun endAnimation() {
        mAnimationArray.clear()
    }

    override fun next(index: Int) {
        mCallback.next(index, mId, mColorEnum.id)
    }

    interface OnAnimationCallback {
        fun next(index: Int, id: Int, color: Int)
    }
}