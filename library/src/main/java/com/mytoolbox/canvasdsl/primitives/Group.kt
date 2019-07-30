package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import android.graphics.Paint
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")

open class Group(private val defNode: Node) : Node(), NodeFabric {
    override fun <T : Node> initNode(node: T, init: T.() -> Unit) {
        node.paint = Paint(paint)
        node.id = genId(node)
        node.init()
        addChild(node)
    }

    override fun group(init: Group.() -> Unit) =
        initNode(Group(defNode), init)

    override fun oval(init: Oval.() -> Unit) =
        initNode(Oval(), init)

    override fun path(init: Path.() -> Unit) =
        initNode(Path(), init)

    override fun rect(init: Rect.() -> Unit) =
        initNode(Rect(), init)

    override fun text(init: Text.() -> Unit) =
        initNode(Text(defNode), init)

    override fun ref(tag: String, init: Ref.() -> Unit) =
        initNode(Ref(defNode).apply { this.tag = tag }, init)

    override fun bitmap(init: Bitmap.() -> Unit) =
        initNode(Bitmap(), init)

    override fun drawable(init: Drawable.() -> Unit) =
        initNode(Drawable(), init)

    override fun drawSelf(canvas: Canvas) {
        children.forEach { it.draw(canvas) }
    }
}