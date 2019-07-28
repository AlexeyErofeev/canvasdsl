package com.mytoolbox.canvasdsl.common

import com.mytoolbox.canvasdsl.primitives.*


interface NodeFabric {
    fun <T : Node> initNode(node: T, init: T.() -> Unit): T
    fun group(init: Group.() -> Unit): Group
    fun path(init: Path.() -> Unit): Path
    fun rect(init: Rect.() -> Unit): Rect
    fun text(init: Text.() -> Unit): Text
    fun ref(tag: String = "", init: Ref.() -> Unit): Ref
    fun oval(init: Oval.() -> Unit): Oval
    fun bitmap(init: Bitmap.() -> Unit): Bitmap
}