package com.mytoolbox.canvasdsl.primitives


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.drawable(init: DrawingDrawable.() -> Unit) =
    initNode(DrawingDrawable(), init)

@Suppress("MemberVisibilityCanBePrivate")
class DrawingDrawable : Node() {
    private var resId: Int = 0

    var drawable: Drawable? = null

    override fun drawSelf(canvas: Canvas) {
        drawable?.draw(canvas)
    }

    @Suppress("unused")
    var Context.res: Int
        get() = resId
        set(resId) {
            this@DrawingDrawable.resId = resId

            @Suppress("DEPRECATION")
            drawable = (
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        resources.getDrawable(resId, theme) else resources.getDrawable(resId))
                .apply {
                    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                }
        }
}