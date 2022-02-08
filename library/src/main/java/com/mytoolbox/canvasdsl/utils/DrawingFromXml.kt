package com.mytoolbox.canvasdsl.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.canvasdsl.primitives.Group
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.canvasdsl.primitives.path
import com.mytoolbox.canvasdsl.utils.parser.Utils
import com.mytoolbox.canvasdsl.utils.parser.pathFromString
import org.xmlpull.v1.XmlPullParser
import java.util.*

fun Context.drawingFromXml(resId: Int) = drawing {
    val xpp = resources.getXml(resId)
    val groups = Stack<Group>()
    var attrPos: Int
    var attrVal: String
    while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
        when (xpp.eventType) {
            XmlPullParser.START_TAG -> {
                when (xpp.name) {
                    "vector" -> {
                        groups.push(rootNode)
                        viewport {
                            attrPos = xpp.getAttrPosition("viewportWidth")
                            if (attrPos != -1)
                                width = xpp.getAttributeValue(attrPos).toFloat()
                            attrPos = xpp.getAttrPosition("viewportHeight")
                            if (attrPos != -1)
                                height = xpp.getAttributeValue(attrPos).toFloat()
                        }

                        attrPos = xpp.getAttrPosition("alpha")
                        if (attrPos != -1)
                            alpha = (xpp.getAttributeValue(attrPos).toFloat() * 255.0f).toInt()

                        attrPos = xpp.getAttrPosition("width")
                        if (attrPos != -1) {
                            attrVal = xpp.getAttributeValue(attrPos)
                            width = if (attrVal.contains("dip") || attrVal.contains("dp"))
                                Utils.getFloatFromDimensionString(attrVal).dp.toInt()
                            else
                                Utils.getFloatFromDimensionString(attrVal).toInt()
                        }

                        attrPos = xpp.getAttrPosition("height")
                        if (attrPos != -1) {
                            attrVal = xpp.getAttributeValue(attrPos)
                            height = if (attrVal.contains("dip") || attrVal.contains("dp"))
                                Utils.getFloatFromDimensionString(attrVal).dp.toInt()
                            else
                                Utils.getFloatFromDimensionString(attrVal).toInt()
                        }

                        if (height == 0 || width == 0)
                            fitToHost = true
                    }

                    "group" -> {
                        groups.peek().group {
                            attrPos = xpp.getAttrPosition("name")
                            if (attrPos != -1)
                                id = xpp.getAttributeValue(attrPos)

                            attrPos = xpp.getAttrPosition("rotation")
                            if (attrPos != -1)
                                rotate(xpp.getAttributeValue(attrPos).toFloat())

                            var sx = 1f
                            var sy = 1f

                            attrPos = xpp.getAttrPosition("scaleX")
                            if (attrPos != -1)
                                sx = xpp.getAttributeValue(attrPos).toFloat()

                            attrPos = xpp.getAttrPosition("scaleY")
                            if (attrPos != -1)
                                sy = xpp.getAttributeValue(attrPos).toFloat()

                            scale(sx, sy)

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

                            relative {
                                pivot(px.vpX, py.vpY)
                                translate(tx.vpX, ty.vpY)
                            }

                            groups.push(this)
                        }

                    }

                    "clip-path" -> {
                        attrPos = xpp.getAttrPosition("pathData")
                        if (attrPos != -1)
                            groups.peek().clipPath {
                                groups.peek().relative {
                                    pathFromString(this@clipPath, xpp.getAttributeValue(attrPos))
                                }
                            }
                    }

                    "path" -> {
                        var hasStroke = false
                        val strokePaint = Paint().apply {
                            attrPos = xpp.getAttrPosition("strokeAlpha")
                            alpha =  (xpp.getAttributeValue(attrPos).toFloat() * 255.0).toInt()
                            attrPos = xpp.getAttrPosition("strokeColor")
                            color = Utils.getColorFromString(xpp.getAttributeValue(attrPos))
                            attrPos = xpp.getAttrPosition("strokeLineCap")
                            strokeCap = Utils.getLineCapFromString(xpp.getAttributeValue(attrPos))
                            attrPos = xpp.getAttrPosition("strokeLineJoin")
                            strokeJoin = Utils.getLineJoinFromString(xpp.getAttributeValue(attrPos))
                            attrPos = xpp.getAttrPosition("strokeMiterLimit")
                            strokeMiter = xpp.getAttributeValue(attrPos).toFloat()
                            attrPos = xpp.getAttrPosition("strokeWidth")
                            strokeWidth = xpp.getAttributeValue(attrPos).toFloat()
                        }

                        val fillPaint = Paint().apply {
                            attrPos = xpp.getAttrPosition("fillAlpha")
                            //Float.parseFloat(xpp.getAttributeValue(attrPos))
                            attrPos = xpp.getAttrPosition("fillColor");
                            // Utils.getColorFromString(xpp.getAttributeValue(attrPos))
                            attrPos = xpp.getAttrPosition("fillType");
                            //Utils.getFillTypeFromString(xpp.getAttributeValue(attrPos))

                        }

                        attrPos = xpp.getAttrPosition("trimPathEnd");
                        //Float.parseFloat(xpp.getAttributeValue(attrPos))
                        attrPos = xpp.getAttrPosition("trimPathOffset")
                        //Float.parseFloat(xpp.getAttributeValue(attrPos))
                        attrPos = xpp.getAttrPosition("trimPathStart")
                        //Float.parseFloat(xpp.getAttributeValue(attrPos))

                        val p = Path()

                        groups.peek().path {
                            attrPos = xpp.getAttrPosition("name")

                            if (attrPos != -1)
                                id = xpp.getAttributeValue(attrPos) + "_fill"

                            paint = fillPaint

                            attrPos = xpp.getAttrPosition("pathData")
                            if (attrPos != -1) {
                                relative {
                                    pathFromString(p, xpp.getAttributeValue(attrPos));
                                }
                                path = p
                            }
                        }
                        if (!p.isEmpty)
                            groups.peek().path {
                                attrPos = xpp.getAttrPosition("name")

                                if (attrPos != -1)
                                    id = xpp.getAttributeValue(attrPos) + "_stroke"

                                paint = strokePaint
                                path = p
                            }
                    }
                }
            }

            XmlPullParser.END_TAG -> {
                when (xpp.name) {
                    "vector" -> {
                    }
                    "group" -> groups.pop()
                    "clip-path" -> {
                    }
                    "path" -> {
                    }
                }
            }
        }

        xpp.next()
    }
}

private fun XmlPullParser.getAttrPosition(attrName: String): Int {
    for (i: Int in 0 until attributeCount)
        if (getAttributeName(i) == attrName) return i

    return -1;
}