package io.github.kurramkurram.futaltacticalboard.ui.animation

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView

const val START_BUTTON_DURATION = 100L
const val START_BUTTON_OFFSET = 100L

fun blinkText(view: TextView, duration: Long, offset: Long) {
    val alphaAnimation = AlphaAnimation(0.1f, 1.0f)
    alphaAnimation.startOffset = offset
    alphaAnimation.duration = duration
    alphaAnimation.repeatMode = Animation.REVERSE
    alphaAnimation.repeatCount = Animation.INFINITE

    view.animation = alphaAnimation
}