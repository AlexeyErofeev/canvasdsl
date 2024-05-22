package com.mytoolbox.canvasdsl.example.elements

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.*
import java.util.*


val linePaint = Paint().apply {
    color = Color.BLACK
    strokeWidth = 6.dp
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
}

val chartPaint = Paint().apply {
    color = Color.BLACK
    style = Paint.Style.FILL
}

fun DrawingGroup.chart() = group {
    path {
        commands {
            moveTo(0f, 96.dp)
            lineTo(166.dp, 96.dp)
        }

        paint = linePaint
    }

    group {
        translate(14.dp, 96.dp)
        scale(1f, -1f)

        for (i in 0..3) {
            rect {
                id = "chart_$i"
                translate(30.dp * i + 6.dp * i, 0f)
                width = 30.dp
                height = 90.dp

                roundedCorners {
                    r = 6.dp
                    top = false // scale changed top to bottom for comfortable chart from x-axis
                }

                paint = chartPaint
            }
        }
    }
}

fun Drawing.animateChart(): Animator = ValueAnimator.ofInt(0, 6).apply {
    // step animation: one iteration in 150ms
    duration = 900
    repeatMode = ValueAnimator.RESTART
    interpolator = LinearInterpolator()
    repeatCount = ValueAnimator.INFINITE

    val rand = Random()
    var prev = 0

    addUpdateListener {
        if (prev == it.animatedValue) // distinct animated value
            return@addUpdateListener

        prev = it.animatedValue as Int

        for (i in 0..3) {
            val chart = rootNode.nodeById<DrawingRect>("chart_$i")
            val nextHeight = rand.nextInt(90.dp.toInt()).toFloat()

            ValueAnimator.ofFloat(chart.height, nextHeight).apply {
                // nested animation: one iteration from current height to nextHeight with duration 900/6 = 150ms
                duration = 150
                repeatCount = 1
                interpolator = FastOutSlowInInterpolator()

                addUpdateListener { nested ->
                    chart.height = nested.animatedValue as Float
                    invalidateSelf()
                }

                start()
            }
        }
    }

    start()
}
