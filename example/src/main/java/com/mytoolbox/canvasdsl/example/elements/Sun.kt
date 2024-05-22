package com.mytoolbox.canvasdsl.example.elements

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.LinearInterpolator
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.DrawingGroup
import com.mytoolbox.canvasdsl.primitives.drawable
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.example.R

context(Context)
fun DrawingGroup.sun(block: (DrawingGroup.() -> Unit)? = null) = group {
    id = "sun_group"
    translate(4.dp, 4.dp)
    pivot(20.dp, 20.dp)
    drawable { res = R.drawable.sun }
    block?.invoke(this)
}

fun Drawing.animateSun(): Animator = ValueAnimator.ofFloat(0f, 90f).apply {
    val sun = rootNode.nodeById<DrawingGroup>("sun_group")

    repeatMode = ValueAnimator.RESTART
    interpolator = LinearInterpolator()
    repeatCount = ValueAnimator.INFINITE
    duration = 2000

    addUpdateListener {
        val angle = it.animatedValue as Float
        sun.rotate(angle)
        invalidateSelf()
    }
    start()
}

