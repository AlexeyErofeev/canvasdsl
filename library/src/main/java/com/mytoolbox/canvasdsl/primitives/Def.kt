package com.mytoolbox.canvasdsl.primitives

import com.mytoolbox.canvasdsl.common.Node

@Suppress("unused")

class Def(defNode: Node) : Group(defNode) {
    var tag
        get() = id
        set(tag) {
            id = tag
        }
}