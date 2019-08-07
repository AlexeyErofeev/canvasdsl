package com.mytoolbox.canvasdsl.example

import android.content.Context
import android.graphics.Paint
import com.mytoolbox.canvasdsl.common.color
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.canvasdsl.primitives.rect
import com.mytoolbox.example.R


fun Context.example() = drawing {
    fitToHost = true

    viewport {
        width = 100f
        height = 100f
        margin = 16.dp
    }

    rect {
        roundedCorners { r = 4.dp }

        relative {
            translate(25.vpX, 25.vpY)
            width = 50.vpX
            height = 50.vpY
        }

        paint {
            isAntiAlias = true
            color = color(R.color.colorPrimary)
            style = Paint.Style.FILL
        }
    }
}