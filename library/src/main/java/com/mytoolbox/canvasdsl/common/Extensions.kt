@file:Suppress("unused")

package com.mytoolbox.canvasdsl.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build

val dpi: Float by lazy { Resources.getSystem().displayMetrics.density }

val Int.dp: Float get() = this * dpi
val Float.dp: Float get() = this * dpi

fun Context.color(id: Int) = with(resources) {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        getColor(id, theme)
    else
        getColor(id)
}

fun Context.drawable(res: Int): Drawable? = with(resources) {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
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