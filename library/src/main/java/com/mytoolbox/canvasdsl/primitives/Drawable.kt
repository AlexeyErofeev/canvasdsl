package com.mytoolbox.canvasdsl.primitives


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.Node

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

            drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                resources.getDrawable(resId, theme)
            else
                resources.getDrawable(resId)
        }
}