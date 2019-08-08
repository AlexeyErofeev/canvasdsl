package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.*

@Suppress("unused")
fun NodeFabric.frame(init: Group.() -> Unit) =
    initNode(Group(defNode), init)

@Suppress("unused")
class Frame(override val defNode: Node) : Group(defNode), ViewportHost {
    private var drawingViewport = DrawingViewport(this)
    override var width: Int = 0
    override var height: Int = 0

    override fun initViewport(viewport: Viewport) {
        viewportFun?.invoke(viewport)
        val subViewport = drawingViewport.relative()

        children.forEach { it.initViewport(subViewport) }
    }

    override fun drawSelf(canvas: Canvas) {
        with(drawingViewport) {
            canvas.translate(marginLeft, marginTop)
        }

        children.forEach { it.draw(canvas) }
    }

    override fun viewport(init: DrawingViewport.() -> Unit) {
        drawingViewport.init()
    }
}