package com.mytoolbox.canvasdsl.utils

import android.content.Context
import android.graphics.Paint
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.DrawingGroup
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.canvasdsl.primitives.path
import com.mytoolbox.canvasdsl.utils.parser.Utils
import com.mytoolbox.canvasdsl.utils.parser.pathFromString
import org.xmlpull.v1.XmlPullParser
import java.util.*

context(Context)
fun DrawingGroup.groupFromDrawable(resId: Int, block: (DrawingGroup.() -> Unit)? = null) = this.group {
    val xpp = resources.getXml(resId)
    val groups = Stack<DrawingGroup>()
    var vectorAlpha = 0
    var attrPos: Int
    var attrVal: String
    var iWidth = 0
    var iHeight = 0
    var vWidth = 0f
    var vHeight = 0f
    var kX = 1f
    var kY = 1f
    var trimPathEnd = 1f
    var trimPathStart = 0f
    var trimPathOffset = 0f

    while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
        when (xpp.eventType) {
            XmlPullParser.START_TAG -> {
                when (xpp.name) {
                    "vector" -> {
                        groups.push(this)

                        attrPos = xpp.getAttrPosition("viewportWidth")
                        if (attrPos != -1)
                            vWidth = xpp.getAttributeValue(attrPos).toFloat()
                        attrPos = xpp.getAttrPosition("viewportHeight")
                        if (attrPos != -1)
                            vHeight = xpp.getAttributeValue(attrPos).toFloat()

                        attrPos = xpp.getAttrPosition("alpha")

                        if (attrPos != -1)
                            vectorAlpha = (xpp.getAttributeValue(attrPos).toFloat() * 255.0f).toInt()

                        attrPos = xpp.getAttrPosition("width")
                        if (attrPos != -1) {
                            attrVal = xpp.getAttributeValue(attrPos)
                            iWidth = if (attrVal.contains("dip") || attrVal.contains("dp"))
                                Utils.getFloatFromDimensionString(attrVal).dp.toInt()
                            else
                                Utils.getFloatFromDimensionString(attrVal).toInt()
                        }

                        attrPos = xpp.getAttrPosition("height")
                        if (attrPos != -1) {
                            attrVal = xpp.getAttributeValue(attrPos)
                            iHeight = if (attrVal.contains("dip") || attrVal.contains("dp"))
                                Utils.getFloatFromDimensionString(attrVal).dp.toInt()
                            else
                                Utils.getFloatFromDimensionString(attrVal).toInt()
                        }
                        kX = iWidth / vWidth
                        kY = iHeight / vHeight

                        paint {
                            alpha = vectorAlpha
                        }
                    }

                    "group" -> {
                        groups.peek().group {
                            groups.push(this)
                            attrPos = xpp.getAttrPosition("name")
                            if (attrPos != -1)
                                id = xpp.getAttributeValue(attrPos)

                            var a = 0f
                            attrPos = xpp.getAttrPosition("rotation")
                            if (attrPos != -1)
                                a = xpp.getAttributeValue(attrPos).toFloat()

                            var sx = 1f
                            var sy = 1f

                            attrPos = xpp.getAttrPosition("scaleX")
                            if (attrPos != -1)
                                sx = xpp.getAttributeValue(attrPos).toFloat()

                            attrPos = xpp.getAttrPosition("scaleY")
                            if (attrPos != -1)
                                sy = xpp.getAttributeValue(attrPos).toFloat()

                            var px = 0f
                            var py = 0f

                            attrPos = xpp.getAttrPosition("pivotX")
                            if (attrPos != -1)
                                px = xpp.getAttributeValue(attrPos).toFloat()

                            attrPos = xpp.getAttrPosition("pivotY")
                            if (attrPos != -1)
                                py = xpp.getAttributeValue(attrPos).toFloat()

                            var tx = 0f
                            var ty = 0f

                            attrPos = xpp.getAttrPosition("translateX")

                            if (attrPos != -1)
                                tx = xpp.getAttributeValue(attrPos).toFloat()

                            attrPos = xpp.getAttrPosition("translateY")

                            if (attrPos != -1)
                                ty = xpp.getAttributeValue(attrPos).toFloat()

                            pivot(px, py)
                            translate(tx * kX, ty * kY)
                            rotate(a)
                            scale(sx, sy)

                            groups.push(this)
                        }
                    }

                    "clip-path" -> {
                        attrPos = xpp.getAttrPosition("pathData")
                        if (attrPos != -1)
                            groups.peek().clipPath {
                                this@clipPath.addPath(pathFromString(xpp.getAttributeValue(attrPos), kX, kY))
                            }
                    }

                    "path" -> {
                        var hasStroke = false
                        val strokePaint = Paint().apply {
                            style = Paint.Style.STROKE
                            attrPos = xpp.getAttrPosition("strokeAlpha")
                            if (attrPos != -1) {
                                alpha = (xpp.getAttributeValue(attrPos).toFloat() * 255.0).toInt()
                                hasStroke = true
                            }
                            attrPos = xpp.getAttrPosition("strokeColor")
                            if (attrPos != -1) {
                                color = Utils.getColorFromString(xpp.getAttributeValue(attrPos))
                            }
                            attrPos = xpp.getAttrPosition("strokeLineCap")
                            if (attrPos != -1) {
                                strokeCap = Utils.getLineCapFromString(xpp.getAttributeValue(attrPos))
                            }
                            attrPos = xpp.getAttrPosition("strokeLineJoin")
                            if (attrPos != -1) {
                                strokeJoin = Utils.getLineJoinFromString(xpp.getAttributeValue(attrPos))
                            }
                            attrPos = xpp.getAttrPosition("strokeMiterLimit")
                            if (attrPos != -1) {
                                strokeMiter = xpp.getAttributeValue(attrPos).toFloat()
                            }
                            attrPos = xpp.getAttrPosition("strokeWidth")
                            if (attrPos != -1) {
                                strokeWidth = xpp.getAttributeValue(attrPos).toFloat()
                            }
                        }

                        attrPos = xpp.getAttrPosition("trimPathEnd")
                        if (attrPos != -1)
                            trimPathEnd = xpp.getAttributeValue(attrPos).toFloat()

                        attrPos = xpp.getAttrPosition("trimPathOffset")
                        if (attrPos != -1)
                            trimPathOffset = xpp.getAttributeValue(attrPos).toFloat()

                        attrPos = xpp.getAttrPosition("trimPathStart")
                        if (attrPos != -1)
                            trimPathStart = xpp.getAttributeValue(attrPos).toFloat()

                        var hasFill = false
                        val fillPaint = Paint().apply {
                            style = Paint.Style.FILL
                            attrPos = xpp.getAttrPosition("fillAlpha")
                            if (attrPos != -1) {
                                alpha = (xpp.getAttributeValue(attrPos).toFloat() * 255.0).toInt()
                                hasFill = true
                            }
                            attrPos = xpp.getAttrPosition("fillColor")
                            if (attrPos != -1) {
                                color = Utils.getColorFromString(xpp.getAttributeValue(attrPos))
                                hasFill = true
                            }
                            attrPos = xpp.getAttrPosition("fillType")
                            if (attrPos != -1) {
                                Utils.getFillTypeFromString(xpp.getAttributeValue(attrPos))
                                hasFill = true
                            }

                        }

                        var data: String? = null
                        attrPos = xpp.getAttrPosition("pathData")
                        if (attrPos != -1)
                            data = xpp.getAttributeValue(attrPos)

                        var name: String? = null
                        attrPos = xpp.getAttrPosition("name")
                        if (attrPos != -1)
                            name = xpp.getAttributeValue(attrPos)

                        if (data != null)
                            groups.peek().group {
                                if (name != null)
                                    id = name

                                groups.push(this)
                                if (hasFill) {
                                    path {
                                        id = groups.peek().id + "_fill"
                                        path = pathFromString(data, kX, kY)
                                        paint = fillPaint

                                        if (trimPathOffset != 0f || trimPathEnd != 1f || trimPathStart != 0f)
                                            trimPath(trimPathStart, trimPathEnd, trimPathOffset)
                                    }
                                }

                                if (hasStroke) {
                                    path {
                                        id = groups.peek().id + "_stroke"
                                        path = pathFromString(data, kX, kY)
                                        paint = strokePaint

                                        if (trimPathOffset != 0f || trimPathEnd != 1f || trimPathStart != 0f)
                                            trimPath(trimPathStart, trimPathEnd, trimPathOffset)
                                    }
                                }
                            }
                    }
                }
            }

            XmlPullParser.END_TAG -> {
                when (xpp.name) {
                    "vector", "group", "path" -> groups.pop()
                    "clip-path" -> {}
                }
            }
        }

        xpp.next()
    }

    block?.invoke(this)
}

private fun XmlPullParser.getAttrPosition(attrName: String): Int {
    for (i: Int in 0 until attributeCount)
        if (getAttributeName(i) == attrName) return i

    return -1
}