package com.mytoolbox.canvasdsl.example.elements

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.DrawingGroup
import com.mytoolbox.canvasdsl.primitives.DrawingPath
import com.mytoolbox.canvasdsl.utils.groupFromDrawable
import com.mytoolbox.example.R


context(Context)
fun DrawingGroup.heart(block: (DrawingGroup.() -> Unit)? = null) =
    // convert drawable to primitives which you can control
    groupFromDrawable(R.drawable.heart) {
        id = "heart_group"
        translate(4.dp, 4.dp)
        pivot(20.dp, 20.dp)
        block?.invoke(this)
    }

fun Drawing.animateHeart(): Animator = ValueAnimator.ofFloat(-0.25f, 0f).apply {
    // search created node
    val heart = rootNode.nodeById<DrawingGroup>("heart_group")
    // get path [name]_fill (if path has stroke props then you have path [name]_stroke)
    // if path has stroke and fill - you have two paths
    // you also have additional [name] group around that paths, to manipulate them together
    val heartFill = heart.nodeById<DrawingPath>("heart_fill")

    repeatMode = ValueAnimator.REVERSE
    interpolator = FastOutSlowInInterpolator()
    repeatCount = ValueAnimator.INFINITE
    duration = 500

    addUpdateListener {
        val value = it.animatedValue as Float
        val scale = 1f + value
        heart.scale(scale, scale)
        heartFill.paint.alpha = 255 + (510f * value).toInt()
        // you can use just one separate animation just with invalidateSelf to optimize drawing invalidation,
        // but I don't want to do it here
        invalidateSelf()
    }
    start()
}

