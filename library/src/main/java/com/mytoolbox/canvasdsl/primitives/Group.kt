package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import android.graphics.Paint
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.group(init: Group.() -> Unit) =
    initNode(Group(defNode), init)

@Suppress("unused")
open class Group(override val defNode: Node) : Node(), NodeFabric {
    override fun <T : Node> initNode(node: T, init: T.() -> Unit) {
        node.paint = Paint(paint)
        node.id = genId(node)
        node.init()
        addChild(node)
    }

    override fun drawSelf(canvas: Canvas) {
        mutableChildren.forEach { it.draw(canvas) }
    }
}