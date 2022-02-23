package com.example.testapplication.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

private const val SWIPE_DURATION = 200L
private const val MAX_CARD_ROTATION_DEGREE = 20

/**
 * For the given direction animate the recyclerView's  top element, and call swipeAnimationEndCallback
 * after finishing
 * returns [ValueAnimator] object for created animation, that should be released afterwards to avoid
 * memory leaks
 */
fun RecyclerView.swipeTopElement(direction: Int, swipeAnimationEndCallback: () -> Unit): ValueAnimator? {
    val elementPos = when (size) {
        2 -> {
            1
        }
        1 -> {
            0
        }
        else -> {
            -1
        }
    }

    // for empty list
    if (elementPos == -1) {
        return null
    }
    return this[elementPos].simulateSwipeAnimation(direction, swipeAnimationEndCallback)
}

fun View.updateForNewXCord() {
    y = (- abs(x.toDouble()) * height / (2 * width)).toFloat()
    rotation = x * MAX_CARD_ROTATION_DEGREE / width
}

private fun View.simulateSwipeAnimation(direction: Int, swipeAnimationEndCallback: () -> Unit): ValueAnimator {

    return ObjectAnimator.ofFloat(0f, direction * width.toFloat()).apply {
        duration = SWIPE_DURATION
        addUpdateListener {
            x = it.animatedValue as Float
            updateForNewXCord()
        }
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                removeAllUpdateListeners()
                removeAllListeners()
                swipeAnimationEndCallback.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

        })

        start()
    }
}

fun ValueAnimator.release() {
    cancel()
    removeAllUpdateListeners()
    removeAllListeners()
}