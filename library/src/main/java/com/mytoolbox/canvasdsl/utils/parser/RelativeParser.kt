package com.mytoolbox.canvasdsl.utils.parser

import android.graphics.Path
import android.graphics.RectF
import android.util.Log
import com.mytoolbox.canvasdsl.common.Viewport
import kotlin.math.*

fun pathFromString(s: String, kX: Float, kY: Float): Path {
    val p = Path()
    val n = s.length
    val ph = ParserHelper(s)
    ph.skipWhitespace()
    var lastX = 0f
    var lastY = 0f
    var lastX1 = 0f
    var lastY1 = 0f
    var contourInitialX = 0f
    var contourInitialY = 0f
    val r = RectF()
    var prevCmd = 'm'
    var cmd = 'x'
    while (ph.pos < n) {
        val next = s[ph.pos]
        if (!Character.isDigit(next) && next != '.' && next != '-') {
            cmd = next
            ph.advance()
        } else if (cmd == 'M') { // implied command
            cmd = 'L'
        } else if (cmd == 'm') { // implied command
            cmd = 'l'
        } else { // implied command
            //ignore
        }
        p.computeBounds(r, true)
        // Log.d(TAG, "  " + cmd + " " + r);
        // Util.debug("* Commands remaining: '" + path + "'.");
        var wasCurve = false
        when (cmd) {
            'M', 'm' -> {
                val x = ph.nextFloat() * kX //.vpX
                val y = ph.nextFloat() * kY //.vpY
                if (cmd == 'm') {
                    p.rMoveTo(x, y)
                    lastX += x
                    lastY += y
                } else {
                    p.moveTo(x, y)
                    lastX = x
                    lastY = y
                }
                contourInitialX = lastX
                contourInitialY = lastY
            }

            'Z', 'z' -> {

                /// p.lineTo(contourInitialX, contourInitialY);
                p.close()
                lastX = contourInitialX
                lastY = contourInitialY
            }

            'L', 'l' -> {
                val x = ph.nextFloat() * kX //.vpX
                val y = ph.nextFloat() * kY //.vpY
                if (cmd == 'l') {
                    if ((prevCmd == 'M' || prevCmd == 'm') && x == 0f && y == 0f) {
                        p.addCircle(x, y, 1f, Path.Direction.CW)
                    } else {
                        p.rLineTo(x, y)
                        lastX += x
                        lastY += y
                    }
                } else {
                    if ((prevCmd == 'M' || prevCmd == 'm') && x == lastX && y == lastY) {
                        p.addCircle(x, y, 1f, Path.Direction.CW)
                    } else {
                        p.lineTo(x, y)
                        lastX = x
                        lastY = y
                    }
                }
            }

            'H', 'h' -> {
                val x = ph.nextFloat() * kX//.vpX
                if (cmd == 'h') {
                    p.rLineTo(x, 0f)
                    lastX += x
                } else {
                    p.lineTo(x, lastY)
                    lastX = x
                }
            }

            'V', 'v' -> {
                val y = ph.nextFloat() * kX //.vpY
                if (cmd == 'v') {
                    p.rLineTo(0f, y)
                    lastY += y
                } else {
                    p.lineTo(lastX, y)
                    lastY = y
                }
            }

            'C', 'c' -> {
                wasCurve = true
                var x1 = ph.nextFloat() * kX //.vpX
                var y1 = ph.nextFloat() * kY //.vpY
                var x2 = ph.nextFloat() * kX //.vpX
                var y2 = ph.nextFloat() * kY //.vpY
                var x = ph.nextFloat() * kX //.vpX
                var y = ph.nextFloat() * kY //.vpY
                if (cmd == 'c') {
                    x1 += lastX
                    x2 += lastX
                    x += lastX
                    y1 += lastY
                    y2 += lastY
                    y += lastY
                }
                p.cubicTo(x1, y1, x2, y2, x, y)
                lastX1 = x2
                lastY1 = y2
                lastX = x
                lastY = y
            }

            'S', 's' -> {
                wasCurve = true
                var x2 = ph.nextFloat() * kX//.vpX
                var y2 = ph.nextFloat() * kY //.vpY
                var x = ph.nextFloat() * kX //.vpX
                var y = ph.nextFloat() * kY //.vpY
                if (cmd == 's') {
                    x2 += lastX
                    x += lastX
                    y2 += lastY
                    y += lastY
                }
                val x1 = 2 * lastX - lastX1
                val y1 = 2 * lastY - lastY1
                p.cubicTo(x1, y1, x2, y2, x, y)
                lastX1 = x2
                lastY1 = y2
                lastX = x
                lastY = y
            }

            'A', 'a' -> {
                val rx = ph.nextFloat() * kX //.vpX
                val ry = ph.nextFloat() * kY //.vpY
                val theta = ph.nextFloat()
                val largeArc = ph.nextFloat().toInt()
                val sweepArc = ph.nextFloat().toInt()
                var x = ph.nextFloat() * kX //.vpX
                var y = ph.nextFloat() * kY //.vpY
                if (cmd == 'a') {
                    x += lastX
                    y += lastY
                }
                drawArc(
                    p,
                    lastX.toDouble(),
                    lastY.toDouble(),
                    x.toDouble(),
                    y.toDouble(),
                    rx.toDouble(),
                    ry.toDouble(),
                    theta.toDouble(),
                    largeArc == 1,
                    sweepArc == 1
                )
                lastX = x
                lastY = y
            }

            'T', 't' -> {
                wasCurve = true
                var x = ph.nextFloat() * kX //.vpX
                var y = ph.nextFloat() * kY //.vpY
                if (cmd == 't') {
                    x += lastX
                    y += lastY
                }
                val x1 = 2 * lastX - lastX1
                val y1 = 2 * lastY - lastY1
                p.cubicTo(lastX, lastY, x1, y1, x, y)
                lastX = x
                lastY = y
                lastX1 = x1
                lastY1 = y1
            }

            'Q', 'q' -> {
                wasCurve = true
                var x1 = ph.nextFloat() * kX //.vpX
                var y1 = ph.nextFloat() * kY //.vpY
                var x = ph.nextFloat() * kX //.vpX
                var y = ph.nextFloat() * kY//.vpY
                if (cmd == 'q') {
                    x += lastX
                    y += lastY
                    x1 += lastX
                    y1 += lastY
                }
                p.cubicTo(lastX, lastY, x1, y1, x, y)
                lastX1 = x1
                lastY1 = y1
                lastX = x
                lastY = y
            }

            else -> {
                ph.advance()
            }
        }
        prevCmd = cmd
        if (!wasCurve) {
            lastX1 = lastX
            lastY1 = lastY
        }
        ph.skipWhitespace()
    }

    return p
}

/*
* Elliptical arc implementation based on the SVG specification notes
* Adapted from the Batik library (Apache-2 license) by SAU
*/
private fun drawArc(
    path: Path, x0: Double, y0: Double, x: Double, y: Double, irx: Double,
    iry: Double, iangle: Double, largeArcFlag: Boolean, sweepFlag: Boolean
) {
    var rx = irx
    var ry = iry
    var angle = iangle
    val dx2 = (x0 - x) / 2.0
    val dy2 = (y0 - y) / 2.0
    angle = Math.toRadians(angle % 360.0)
    val cosAngle = cos(angle)
    val sinAngle = sin(angle)
    val x1 = cosAngle * dx2 + sinAngle * dy2
    val y1 = -sinAngle * dx2 + cosAngle * dy2
    rx = abs(rx)
    ry = abs(ry)
    var prx = rx * rx
    var pry = ry * ry
    val px1 = x1 * x1
    val py1 = y1 * y1

    // check that radii are large enough
    val radiiCheck = px1 / prx + py1 / pry
    if (radiiCheck > 1) {
        rx *= sqrt(radiiCheck)
        ry *= sqrt(radiiCheck)
        prx = rx * rx
        pry = ry * ry
    }

    // Step 2 : Compute (cx1, cy1)
    var sign: Double = if (largeArcFlag == sweepFlag) -1.0 else 1.0
    var sq = ((prx * pry - prx * py1 - pry * px1)
            / (prx * py1 + pry * px1))
    sq = if (sq < 0) 0.0 else sq
    val coef = sign * sqrt(sq)
    val cx1 = coef * (rx * y1 / ry)
    val cy1 = coef * -(ry * x1 / rx)
    val sx2 = (x0 + x) / 2.0
    val sy2 = (y0 + y) / 2.0
    val cx = sx2 + (cosAngle * cx1 - sinAngle * cy1)
    val cy = sy2 + (sinAngle * cx1 + cosAngle * cy1)

    // Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
    val ux = (x1 - cx1) / rx
    val uy = (y1 - cy1) / ry
    val vx = (-x1 - cx1) / rx
    val vy = (-y1 - cy1) / ry

    // Compute the angle start
    var n: Double = sqrt(ux * ux + uy * uy)
    var p: Double = ux // (1 * ux) + (0 * uy)
    sign = if (uy < 0) -1.0 else 1.0
    var angleStart = Math.toDegrees(sign * acos(p / n))

    // Compute the angle extent
    n = sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy))
    p = ux * vx + uy * vy
    sign = if (ux * vy - uy * vx < 0) -1.0 else 1.0
    var angleExtent = Math.toDegrees(sign * Math.acos(p / n))
    if (!sweepFlag && angleExtent > 0) {
        angleExtent -= 360.0
    } else if (sweepFlag && angleExtent < 0) {
        angleExtent += 360.0
    }
    angleExtent %= 360.0
    angleStart %= 360.0
    val oval = RectF(
        (cx - rx).toFloat(),
        (cy - ry).toFloat(), (cx + rx).toFloat(), (cy + ry).toFloat()
    )
    path.addArc(oval, angleStart.toFloat(), angleExtent.toFloat())
}