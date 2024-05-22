package com.mytoolbox.canvasdsl.example.elements

import android.graphics.Color
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.DrawingText
import com.mytoolbox.canvasdsl.primitives.text

fun Drawing.caption(string: String, block: DrawingText.() -> Unit) = text {
    text = string

    paint {
        color = Color.BLACK
        textSize = 20.dp
    }

    block()
}