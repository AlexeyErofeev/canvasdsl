package com.mytoolbox.canvasdsl.primitives

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.SizeF
import com.mytoolbox.canvasdsl.common.Viewport

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Text(private val defNode: Node) : Node() {
    private var path: android.graphics.Path? = null
    private var resId = 0
    var text = ""

    var size: SizeF = SizeF(-1f, -1f)
        get() =
            if (field.height >= 0 || field.width >= 0)
                field
            else
                throw RuntimeException("use 'size' value from 'relative{}' section")
        private set

    var Context.res: Int
        get() = resId
        set(id) {
            resId = id
            text = getString(id)
        }

    class OnPath {
        var tag: String = ""
        var hOffset: Float = 0f
        var vOffset: Float = 0f
    }

    var onPath = OnPath()

    override fun drawSelf(canvas: Canvas) = with(onPath) {
        path?.let {
            canvas.drawTextOnPath(text, it, hOffset, vOffset, paint)
        } ?: run {
            canvas.drawText(text, 0f, if (size.height < 0f) 0f else size.height, paint)
        }
    }

    override fun initViewport(viewport: Viewport) {
        with(Rect().apply {
            paint.getTextBounds(text, 0, text.count(), this)
        }) {
            size = SizeF(width().toFloat(), height().toFloat())
        }
        super.initViewport(viewport)
    }

    fun path(init: OnPath.() -> Unit) = with(onPath) {
        init()
        path = defNode.nodeById<Path>(tag).path
    }
}
