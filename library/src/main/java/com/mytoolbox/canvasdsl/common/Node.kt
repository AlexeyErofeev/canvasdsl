package com.mytoolbox.canvasdsl.common

import android.graphics.*
import com.mytoolbox.canvasdsl.utils.newRoundedRect

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class Node : ViewportGuest {
    protected var pivot = PointF(0f, 0f)
    protected var pos = PointF(0f, 0f)
    protected var angle = 0f
    protected var scale = PointF(1f, 1f)
    protected var clipRegion = Path()
    protected var viewportFun: (Viewport.() -> Unit)? = null
    protected var mutableChildren = mutableListOf<Node>()

    val children: List<Node> get() = mutableChildren

    var paint: Paint = Paint()
    var id = ""

    fun draw(canvas: Canvas) = with(canvas) {
        save()

        translate(pos.x, pos.y)
        rotate(angle, pivot.x, pivot.y)
        scale(scale.x, scale.y, pivot.x, pivot.y)

        if (!clipRegion.isEmpty)
            clipPath(clipRegion)

        drawSelf(canvas)

        restore()
    }

    override fun initViewport(viewport: Viewport) {
        viewportFun?.invoke(viewport)
        mutableChildren.forEach { it.initViewport(viewport) }
    }

    open fun drawSelf(canvas: Canvas) {}

    fun paint(init: Paint.() -> Unit): Paint = paint.apply { init() }

    fun translate(x: Float, y: Float) {
        this.pos.x = x
        this.pos.y = y
    }

    fun pivot(x: Float, y: Float) {
        this.pivot.x = x
        this.pivot.y = y
    }

    fun rotate(degree: Float) {
        this.angle = degree
    }

    fun scale(xs: Float, ys: Float) {
        this.scale.x = xs
        this.scale.y = ys
    }

    fun clipRect(left: Float, top: Float, right: Float, bottom: Float, rx: Float = 0f, ry: Float = 0f) {
        val rounded = rx > 0f && ry > 0f
        clipRegion = newRoundedRect(left, top, right, bottom, rx, ry, rounded, rounded, rounded, rounded)
    }

    fun clipPath(init: Path.() -> Unit) {
        clipRegion = Path().apply { init() }
    }

    fun clipPath(path: Path) {
        clipRegion = path
    }

    override fun relative(init: Viewport.() -> Unit) {
        viewportFun = init
    }

    fun addChild(node: Node) {
        mutableChildren.withIndex().find { it.value.id == node.id }?.let {
            mutableChildren[it.index].removeAll()
            mutableChildren[it.index] = node
        } ?: run {
            mutableChildren.add(node)
        }
    }

    fun removeAll() {
        mutableChildren.forEach { it.removeAll() }
        mutableChildren.clear()
    }

    fun <T : Node> nodeById(id: String): T =
        findChild(id) ?: throw RuntimeException("can't find id '$id'")

    @Suppress("UNCHECKED_CAST")
    private fun <T : Node> findChild(id: String): T? {
        if (this.id == id)
            return this as T

        mutableChildren.forEach {
            it.findChild<T>(id)?.let { res -> return res }
        }

        return null
    }

    protected fun genId(node: Node): String = node.javaClass.simpleName + "_" + System.identityHashCode(node)
}