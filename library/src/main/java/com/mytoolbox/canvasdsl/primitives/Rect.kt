@file:Suppress("MemberVisibilityCanBePrivate")

package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import android.graphics.Path
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric
import com.mytoolbox.canvasdsl.common.Viewport
import com.mytoolbox.canvasdsl.utils.newRoundedRect

@Suppress("unused")
fun NodeFabric.rect(init: Rect.() -> Unit) =
    initNode(Rect(), init)

@Suppress("unused")
class Rect : Node() {
    var width: Float = 0f
    var height: Float = 0f

    private var roundedCorners = false
    private lateinit var path: Path

    @Suppress("MemberVisibilityCanBePrivate")
    private val rounded = RoundedCorners()

    override fun drawSelf(canvas: Canvas) {
        if (roundedCorners)
            canvas.drawPath(path, paint)
        else
            canvas.drawRect(0f, 0f, width, height, paint)
    }

    fun roundedCorners(init: RoundedCorners.() -> Unit) {
        roundedCorners = true
        rounded.all = true
        rounded.init()
    }

    override fun initViewport(viewport: Viewport) {
        super.initViewport(viewport)
        if (roundedCorners)
            with(rounded) {
                path = newRoundedRect(
                    0f, 0f, width, height,
                    rx, ry, topLeft, topRight, bottomRight, bottomLeft
                )
            }
    }

    inner class RoundedCorners {
        var r: Float = 0f
            set(r) {
                field = if (rx < 0) 0f else rx
                rx = r
                ry = r
            }

        var rx: Float = 0f
            set(rx) {
                field = if (rx < 0) 0f else rx
            }
        var ry: Float = 0f
            set(ry) {
                field = if (ry < 0) 0f else ry
            }

        var topLeft = false
        var topRight = false
        var bottomLeft = false
        var bottomRight = false

        var top
            get() = topLeft && topRight
            set(top) {
                topLeft = top
                topRight = top
            }

        var bottom
            get() = bottomLeft && bottomRight
            set(bottom) {
                bottomLeft = bottom
                bottomRight = bottom
            }

        var left
            get() = bottomLeft && topLeft
            set(left) {
                bottomLeft = left
                topLeft = left
            }

        var right
            get() = bottomRight && topRight
            set(right) {
                bottomRight = right
                topRight = right
            }

        var all
            get() = top && bottom
            set(all) {
                top = all
                bottom = all
            }

        val any: Boolean
            get() = topLeft || topRight || bottomLeft || bottomRight
    }
}