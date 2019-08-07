package com.mytoolbox.canvasdsl.common


interface NodeFabric {
    val defNode: Node
    fun <T : Node> initNode(node: T, init: T.() -> Unit)
}