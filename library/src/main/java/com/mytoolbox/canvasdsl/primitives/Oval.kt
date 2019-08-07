package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.Node
import android.graphics.RectF
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")

fun NodeFabric.oval(init: Oval.() -> Unit) =
    initNode(Oval(), init)

class Oval : Node() {
    private var rect: RectF = RectF(0f, 0f, 0f, 0f)

    var width: Float = 0f
        set(value) {
            rect.right = value
            field = value
        }

    var height: Float = 0f
        set(value) {
            rect.bottom = value
            field = value
        }

    override fun drawSelf(canvas: Canvas) {
        canvas.drawOval(rect, paint)
    }
}