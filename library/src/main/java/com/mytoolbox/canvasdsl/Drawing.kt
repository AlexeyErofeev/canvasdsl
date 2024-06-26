package com.mytoolbox.canvasdsl

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.mytoolbox.canvasdsl.primitives.DrawingDef
import com.mytoolbox.canvasdsl.primitives.DrawingGroup
import com.mytoolbox.canvasdsl.common.*

@Suppress("unused")
fun Context.drawing(init: Drawing.() -> Unit): Lazy<Drawing> = lazy { Drawing(context = this).apply(init) }

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Drawing(val context: Context, override val defNode: Node = Node(), val rootNode: DrawingGroup = DrawingGroup(defNode).apply { id = "root" }) : Drawable(),
    NodeFabric by rootNode, ViewportHost {
    override var width: Int = 0
    override var height: Int = 0

    var fitToHost = false

    private var filled = false
    private var drawingViewport = DrawingViewport(this)

    // Drawable implementation
    override fun draw(canvas: Canvas) {
        if (filled)
            canvas.drawPaint(rootNode.paint)

        canvas.translate(drawingViewport.marginLeft, drawingViewport.marginTop)
        rootNode.draw(canvas)
    }
    
    override fun onBoundsChange(bounds: Rect) {
        if (fitToHost) {
            width = bounds.width()
            height = bounds.height()
        }

        rootNode.initViewport(drawingViewport.relative())
        invalidateSelf()
    }

    override fun getIntrinsicWidth(): Int = width
    override fun getIntrinsicHeight(): Int = height

    override fun setAlpha(alpha: Int) {
        rootNode.paint.alpha = alpha
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        rootNode.paint.colorFilter = colorFilter
    }

    // ViewportHost implementation
    override fun viewport(init: DrawingViewport.() -> Unit) {
        drawingViewport.init()
    }

    // Own
    fun define(tag: String = "", init: DrawingDef.() -> Unit): DrawingDef {
        val def = DrawingDef(defNode)
        def.tag = tag
        def.init()
        defNode.addChild(def)

        return def
    }

    fun fill(init: Paint.() -> Unit) {
        rootNode.paint(init)
        filled = true
    }
}