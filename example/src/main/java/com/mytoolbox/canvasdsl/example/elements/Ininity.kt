package com.mytoolbox.canvasdsl.example.elements

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.DrawingGroup
import com.mytoolbox.canvasdsl.primitives.DrawingPath
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.canvasdsl.primitives.path
import com.mytoolbox.canvasdsl.utils.parser.pathFromString

// use pathData from svg with x/y scale coefficients (for exact size use image_width/viewport_width)
val infPath = pathFromString("M1 18C1 4.99999 10 -2 21 2C32 6 50.5 29 63 34C75 38.8 83 29.5 83 18C83 6.5 74 -2.5 63 2C52 6.5 31 29.5 21 34C11 38.5 1 31 1 18Z", 2.dp, 2.dp)

val infPaint = Paint().apply {
    color = Color.BLACK
    style = Paint.Style.STROKE
    strokeWidth = 6.dp
    strokeCap = Paint.Cap.ROUND
}

fun DrawingGroup.infinity() = group {
    path {
        id = "base"
        path = infPath
        paint = infPaint
    }

    path {
        id = "tail"
        path = infPath
        paint = infPaint
    }
}

fun Drawing.animateInfinity(): Animator = ValueAnimator.ofFloat(0f, 1f).apply {
    val base = rootNode.nodeById<DrawingPath>("base")
    val tail = rootNode.nodeById<DrawingPath>("tail")

    duration = 1500
    repeatMode = ValueAnimator.RESTART
    interpolator = LinearInterpolator()
    repeatCount = ValueAnimator.INFINITE

    addUpdateListener {
        val value = it.animatedValue as Float
        base.trimPath(0f, 0.5f, value)
        tail.trimPath(0f, 0.5f, value - 1f)
    }

    start()
}