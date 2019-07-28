package com.mytoolbox.canvasdsl.common

import android.graphics.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class Node {
    protected var pivot = PointF(0f, 0f)
    protected var pos = PointF(0f, 0f)
    protected var angle = 0f
    protected var scale = PointF(1f, 1f)
    protected var clipRegion = Path()
    protected var viewportFun: (Viewport.() -> Unit)? = null
    protected var children = mutableListOf<Node>()

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

    open fun initViewport(viewport: Viewport) {
        viewportFun?.invoke(viewport)
        children.forEach { it.initViewport(viewport) }
    }

    open fun drawSelf(canvas: Canvas) {}

    fun paint(init: Paint.() -> Unit): Paint = paint.apply { init() }

    fun translate(x: Float, y: Float) {
        this.pos.x = x
        this.pos.y = y
    }

    fun pivot(x: Float, y: Float) {
        this.pos.x = x
        this.pos.y = y
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

    fun relative(init: Viewport.() -> Unit) {
        viewportFun = init
    }

    fun addChild(node: Node) {
        children.withIndex().find { it.value.id == node.id }?.let {
            children[it.index].removeAll()
            children[it.index] = node
        } ?: run {
            children.add(node)
        }
    }

    fun removeAll() {
        children.forEach { it.removeAll() }
        children.clear()
    }

    fun <T : Node> nodeById(id: String): T =
        findChild(id) ?: throw RuntimeException("can't find id '$id'")

    @Suppress("UNCHECKED_CAST")
    private fun <T : Node> findChild(id: String): T? {
        if (this.id == id)
            return this as T

        children.forEach {
            it.findChild<T>(id)?.let { res -> return res }
        }

        return null
    }

    protected fun genId(node: Node): String = node.javaClass.simpleName + "_" + System.identityHashCode(node)
}