@file:Suppress("unused")

package com.mytoolbox.canvasdsl.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build

val dpi: Float by lazy { Resources.getSystem().displayMetrics.density }

val Int.dp: Float get() = this * dpi
val Float.dp: Float get() = this * dpi

fun Context.color(id: Int) = with(resources) {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        getColor(id, theme)
    else
        getColor(id)
}

fun Context.drawable(res: Int): Drawable? = with(resources) {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        getDrawable(res, theme)
    else
        getDrawable(res)
}

fun paint(init: Paint.() -> Unit): Paint = Paint().apply { init() }

fun Context.renderDrawable(resId: Int, width: Int? = null, height: Int? = null): Bitmap? {
    drawable(resId)?.let {
        val xs: Int = width ?: it.intrinsicWidth
        val ys: Int = height ?: it.intrinsicHeight

        if (it is BitmapDrawable)
            return it.bitmap

        val bitmap = Bitmap.createBitmap(xs, ys, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        it.setBounds(0, 0, xs, ys)
        it.draw(canvas)

        return bitmap
    }
    return null
}

fun newRoundedRect(
    left: Float, top: Float, right: Float, bottom: Float, rX: Float, rY: Float,
    tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean
): Path {
    var rx = rX
    var ry = rY
    val path = Path()
    val width = right - left
    val height = bottom - top
    if (rx > width / 2) rx = width / 2
    if (ry > height / 2) ry = height / 2
    val widthMinusCorners = width - 2 * rx
    val heightMinusCorners = height - 2 * ry

    path.moveTo(right, top + ry)
    if (tr)
        path.rQuadTo(0f, -ry, -rx, -ry)//top-right corner
    else {
        path.rLineTo(0f, -ry)
        path.rLineTo(-rx, 0f)
    }
    path.rLineTo(-widthMinusCorners, 0f)

    if (tl)
        path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
    else {
        path.rLineTo(-rx, 0f)
        path.rLineTo(0f, ry)
    }
    path.rLineTo(0f, heightMinusCorners)

    if (bl)
        path.rQuadTo(0f, ry, rx, ry)//bottom-left corner
    else {
        path.rLineTo(0f, ry)
        path.rLineTo(rx, 0f)
    }

    path.rLineTo(widthMinusCorners, 0f)
    if (br)
        path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
    else {
        path.rLineTo(rx, 0f)
        path.rLineTo(0f, -ry)
    }

    path.rLineTo(0f, -heightMinusCorners)

    path.close()//Given close, last line to can be removed.

    return path
}