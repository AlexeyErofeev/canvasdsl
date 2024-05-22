package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.path(init: DrawingPath.() -> Unit) =
    initNode(DrawingPath(), init)

@Suppress("unused")
class DrawingPath : Node() {

    var path = Path()
    private var trimmedPath = Path()
    private var trimmed = false

    private var trimStart = 0f
    private var trimEnd = 1f
    private var trimOffset = 0f

    fun trimPath(trimStart: Float = 0f, trimEnd: Float = 1f, trimOffset: Float = 0f) {
        this.trimStart = trimStart
        this.trimEnd = trimEnd
        this.trimOffset = trimOffset
        trimmed = true

        if (!path.isEmpty)
            trim()
    }

    override fun drawSelf(canvas: Canvas) {
        if (!trimmed)
            canvas.drawPath(path, paint)
        else
            canvas.drawPath(trimmedPath, paint)
    }

    fun commands(init: Path.() -> Unit) {
        path = Path().apply { init() }
    }

    private fun trim() {
        val pathMeasure = PathMeasure(path, true)
        val length = pathMeasure.length
        trimmedPath = Path()

        if (trimStart != trimEnd && trimOffset < 1f)
            pathMeasure.getSegment(
                (trimStart + trimOffset) * length,
                (trimEnd + trimOffset) * length,
                trimmedPath,
                true
            )
    }
}