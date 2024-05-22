package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.ref(tag: String, init: DrawingRef.() -> Unit) =
    initNode(DrawingRef(defNode).apply { this.tag = tag }, init)

@Suppress("unused")
class DrawingRef(private val defNode: Node) : Node() {
    var tag = ""

    override fun drawSelf(canvas: Canvas) {
        defNode.nodeById<DrawingDef>(tag).drawSelf(canvas)
    }
}