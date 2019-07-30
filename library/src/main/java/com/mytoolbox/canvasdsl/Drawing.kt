package com.mytoolbox.canvasdsl

import android.graphics.*
import android.graphics.drawable.Drawable
import com.mytoolbox.canvasdsl.primitives.Def
import com.mytoolbox.canvasdsl.primitives.Group
import com.mytoolbox.canvasdsl.common.*

@Suppress("unused")
fun drawing(init: Drawing.() -> Unit): Lazy<Drawing> = lazy { Drawing().apply(init) }

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Drawing(val defNode: Node = Node(), val rootNode: Group = Group(defNode).apply { id = "root" }) : Drawable(),
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

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        rootNode.paint.colorFilter = colorFilter
    }

    // Own
    fun define(tag: String = "", init: Def.() -> Unit): Def {
        val def = Def(defNode)
        def.tag = tag
        def.init()
        defNode.addChild(def)

        return def
    }

    fun fill(init: Paint.() -> Unit) {
        rootNode.paint(init)
        filled = true
    }

    fun viewport(init: DrawingViewport.() -> Unit) {
        drawingViewport.init()
    }
}