package com.mytoolbox.canvasdsl.primitives


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric
import com.mytoolbox.canvasdsl.common.renderDrawable

@Suppress("unused")
fun NodeFabric.bitmap(init: DrawingBitmap.() -> Unit) =
    initNode(DrawingBitmap(), init)


@Suppress("MemberVisibilityCanBePrivate")
class DrawingBitmap: Node() {
    private var resId: Int = 0

    var bitmap: Bitmap? = null
    var width: Float? = null
    var height: Float? = null

    override fun drawSelf(canvas: Canvas) {
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, paint) }
    }

    @Suppress("unused")
    var Context.res: Int
        get() = resId
        set(resId) {
            this@DrawingBitmap.resId = resId
            bitmap = renderDrawable(resId, width?.toInt(), height?.toInt())
        }
}