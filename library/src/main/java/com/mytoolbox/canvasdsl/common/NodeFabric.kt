package com.mytoolbox.canvasdsl.common

import com.mytoolbox.canvasdsl.primitives.*


interface NodeFabric {
    fun <T : Node> initNode(node: T, init: T.() -> Unit)
    fun group(init: Group.() -> Unit)
    fun path(init: Path.() -> Unit)
    fun rect(init: Rect.() -> Unit)
    fun text(init: Text.() -> Unit)
    fun ref(tag: String = "", init: Ref.() -> Unit)
    fun oval(init: Oval.() -> Unit)
    fun bitmap(init: Bitmap.() -> Unit)
    fun drawable(init: Drawable.() -> Unit)
}