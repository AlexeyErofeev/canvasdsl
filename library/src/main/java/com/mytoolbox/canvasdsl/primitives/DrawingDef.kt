package com.mytoolbox.canvasdsl.primitives

import com.mytoolbox.canvasdsl.common.Node

@Suppress("unused")

class DrawingDef(defNode: Node) : DrawingGroup(defNode) {
    var tag
        get() = id
        set(tag) {
            id = tag
        }
}