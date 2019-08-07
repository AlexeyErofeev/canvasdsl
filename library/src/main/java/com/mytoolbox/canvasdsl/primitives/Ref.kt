package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.Node
import com.mytoolbox.canvasdsl.common.NodeFabric

@Suppress("unused")
fun NodeFabric.ref(tag: String, init: Ref.() -> Unit) =
    initNode(Ref(defNode).apply { this.tag = tag }, init)

@Suppress("unused")
class Ref(private val defNode: Node) : Node() {
    var tag = ""

    override fun drawSelf(canvas: Canvas) {
        defNode.nodeById<Def>(tag).drawSelf(canvas)
    }
}