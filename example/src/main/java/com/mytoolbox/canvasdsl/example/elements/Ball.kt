package com.mytoolbox.canvasdsl.example.elements

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.DrawingGroup
import com.mytoolbox.canvasdsl.primitives.drawable
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.example.R

context(Context)
fun DrawingGroup.ball(block: (DrawingGroup.() -> Unit)? = null) = group {
    id = "ball_group"
    translate(4.dp, 4.dp)
    pivot(20.dp, 20.dp)
    // use drawable as atomic resource
    drawable { res = R.drawable.ball }
    block?.invoke(this)
}

fun Drawing.animateBall(): Animator = ValueAnimator.ofFloat(8.dp, 0.dp).apply {
    val ball = rootNode.nodeById<DrawingGroup>("ball_group")

    repeatMode = ValueAnimator.REVERSE
    interpolator = FastOutSlowInInterpolator()
    repeatCount = ValueAnimator.INFINITE
    duration = 200

    addUpdateListener {
        val height = it.animatedValue as Float
        ball.translate(ball.pos.x, height)
        ball.scale(1f + height / 320f, 1f - height / 640f)
        invalidateSelf()
    }

    start()
}

