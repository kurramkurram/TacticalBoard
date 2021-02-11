package io.github.kurramkurram.futaltacticalboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.futaltacticalboard.ui.animation.PlayerAnimation

class PlayerLayout : LinearLayout, OnPlayerTouchListener.OnUpdateCallbackListener,
    PlayerAnimation.OnAnimationCallback {

    companion object {
        const val TAG = "PlayerLayout"
    }

    lateinit var mName: TextView
    var mId = 0
    var mAnimationArray = ArrayList<Array<Int>>()
    private lateinit var mCallback: OnAnimationCallback
    private val mAnimation = PlayerAnimation()
    private lateinit var mColorEnum: ColorEnum

    private var mX = 0.0F
    private var mY = 0.0F

    private var mDx = 0 // 初期位置からの移動量
    private var mDy = 0 // 初期位置からの移動量

    // 操作中かどうか. true：操作中
    private var mIsMoving = false

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

        mX = x
        mY = y

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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        // Visibilityの切り替えによるViewの更新で初期位置に戻ることを回避
        if (mX != x && mY != y && !mIsMoving) {
            val dx = left + mDx
            val dy = top + mDy
            layout(dx, dy, dx + width, dy + height)
        }
        mIsMoving = false
        mX = x
        mY = y
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    override fun update(deltaPoint: Array<Int>) {
        mIsMoving = true
        mDx += deltaPoint[0]
        mDy += deltaPoint[1]

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