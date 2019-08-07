package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import android.graphics.Path
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.path(init: com.mytoolbox.canvasdsl.primitives.Path.() -> Unit) =
    initNode(com.mytoolbox.canvasdsl.primitives.Path(), init)

@Suppress("unused")
class Path : Node() {
    var path = Path()
        private set

    override fun drawSelf(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    fun commands(init: Path.() -> Unit) {
        path = Path().apply { init() }
    }
}