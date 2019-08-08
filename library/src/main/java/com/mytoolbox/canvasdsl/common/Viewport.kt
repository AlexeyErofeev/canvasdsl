@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.mytoolbox.canvasdsl.common

interface ViewportHost {
    val width: Int
    val height: Int
    fun viewport(init: DrawingViewport.() -> Unit)
}

interface ViewportGuest {
    fun initViewport(viewport: Viewport)
    fun relative(init: Viewport.() -> Unit)
}

open class ViewportParams(
        open val width: Float = 1f,
        open val height: Float = 1f,
        open val marginLeft: Float = 0f,
        open val marginRight: Float = 0f,
        open val marginTop: Float = 0f,
        open val marginBottom: Float = 0f)

open class MutableViewportParams(
        override var width: Float = 1f,
        override var height: Float = 1f,
        override var marginLeft: Float = 0f,
        override var marginRight: Float = 0f,
        override var marginTop: Float = 0f,
        override var marginBottom: Float = 0f) : ViewportParams() {

    var margin
        get() = marginLeft
        set(value) {
            marginLeft = value
            marginRight = value
            marginTop = value
            marginBottom = value
        }

    var marginHorizontal
        get() = marginLeft
        set(value) {
            marginLeft = value
            marginRight = value
        }

    var marginVertical
        get() = marginTop
        set(value) {
            marginTop = value
            marginBottom = value
        }
}

class DrawingViewport(private val host: ViewportHost) : MutableViewportParams() {
    fun relative() = Viewport(host, this)
}

data class Viewport(private val host: ViewportHost, private val vp: ViewportParams) {
    val vpX: Float get() = (host.width - vp.marginLeft - vp.marginRight) / vp.width
    val vpY: Float get() = (host.height - vp.marginTop - vp.marginBottom) / vp.height

    val Int.vpX: Float get() = this * this@Viewport.vpX
    val Int.vpY: Float get() = this * this@Viewport.vpY

    val Float.vpX: Float get() = this * this@Viewport.vpX
    val Float.vpY: Float get() = this * this@Viewport.vpY
}