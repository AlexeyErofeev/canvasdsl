package com.mytoolbox.canvasdsl.primitives

import android.graphics.Canvas
import com.mytoolbox.canvasdsl.common.Node

class Ref(private val defNode: Node) : Node() {
    var tag = ""

    override fun drawSelf(canvas: Canvas) {
        defNode.nodeById<Def>(tag).drawSelf(canvas)
    }
}