package com.mytoolbox.canvasdsl.primitives

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric
import com.mytoolbox.canvasdsl.common.SizeF
import com.mytoolbox.canvasdsl.common.Viewport

/**
 * Tag to draw multiline text.
 *
 * Differences from [text]:
 *  * breaks text in several lines if it doesn't fit (see [MultiLineText.alignment] for align text)
 *  * support `\n` to break lines
 *  * [Node.paint] could be [TextPaint]
 *  * doesn't support drawing with [Path]
 *  * could be slower than [text] implementation
 */
@Suppress("unused")
fun NodeFabric.multiLineText(init: MultiLineText.() -> Unit) =
    initNode(MultiLineText(), init)

@Suppress("MemberVisibilityCanBePrivate")
class MultiLineText : Node() {
    private var resId = 0
    var text = ""
    var alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    private var width: Int = 0

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

    override fun drawSelf(canvas: Canvas) {
        val paint = TextPaint(paint)
        val width = if (width <= 0) canvas.width else width
        val layout = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setIncludePad(false)
                .setAlignment(alignment)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                text, paint, width,
                alignment, 1.0f, 0.0f, false
            )
        }
        layout.draw(canvas)
    }

    override fun initViewport(viewport: Viewport) {
        with(Rect().apply {
            paint.getTextBounds(text, 0, text.count(), this)
        }) {
            size = SizeF(width().toFloat(), height().toFloat())
        }
        width = viewport.vpWidth.toInt()
        super.initViewport(viewport)
    }
}