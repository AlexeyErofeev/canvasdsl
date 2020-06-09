package com.mytoolbox.canvasdsl.primitives


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.drawable(init: com.mytoolbox.canvasdsl.primitives.Drawable.() -> Unit) =
    initNode(com.mytoolbox.canvasdsl.primitives.Drawable(), init)

@Suppress("MemberVisibilityCanBePrivate")
class Drawable : Node() {
    private var resId: Int = 0

    var drawable: Drawable? = null

    override fun drawSelf(canvas: Canvas) {
        drawable?.draw(canvas)
    }

    @Suppress("unused")
    var Context.res: Int
        get() = resId
        set(resId) {
            this@Drawable.resId = resId

            @Suppress("DEPRECATION")
            drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                resources.getDrawable(resId, theme)
            else
                resources.getDrawable(resId)
        }
}