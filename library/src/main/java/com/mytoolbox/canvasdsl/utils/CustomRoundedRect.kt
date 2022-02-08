package com.mytoolbox.canvasdsl.utils

import android.graphics.Path


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