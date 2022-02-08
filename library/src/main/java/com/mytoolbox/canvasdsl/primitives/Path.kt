package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import android.graphics.Path
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric
import com.mytoolbox.canvasdsl.primitives.Path as P

@Suppress("unused")
fun NodeFabric.path(init: P.() -> Unit) =
    initNode(P(), init)

@Suppress("unused")
class Path : Node() {
    var path = Path()

    override fun drawSelf(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    fun commands(init: Path.() -> Unit) {
        path = Path().apply { init() }
    }
}