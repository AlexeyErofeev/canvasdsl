package com.mytoolbox.canvasdsl.primitives


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric
import com.mytoolbox.canvasdsl.common.renderDrawable

@Suppress("unused")
fun NodeFabric.bitmap(init: com.mytoolbox.canvasdsl.primitives.Bitmap.() -> Unit) =
    initNode(Bitmap(), init)


@Suppress("MemberVisibilityCanBePrivate")
class Bitmap: Node() {
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
            this@Bitmap.resId = resId
            bitmap = renderDrawable(resId, width?.toInt(), height?.toInt())
        }
}