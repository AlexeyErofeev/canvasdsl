package com.mytoolbox.canvasdsl.example.chart

import android.content.Context
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.canvasdsl.primitives.*
import com.mytoolbox.example.R


data class DayStress(val low: Int, val medium: Int, val high: Int, val wear: Boolean, val average: Int)

fun Context.stressWeek(data: Array<DayStress>) = drawing {
    val textP by textP()
    val graphLow by stressLow()
    val graphMedium by stressMedium()
    val graphHi by stressHi()

    fitToHost = true

    viewport {
        width = 344f
        height = 144f
    }

    define {
        id = "weekArrow"
        drawable { res = R.drawable.week_arrow }
    }

    text {
        text = "24 h"
        paint = textP
        relative {
            translate(40.vpX - size.width, 16.vpY - size.height / 2)
        }
    }

    group {
        relative {
            translate(56.vpX, 16.vpY)
        }

        paintGrayBars(this@stressWeek)

        group {
            relative { translate(0f, 92.vpY) }
            scale(1f, -1f)

            data.forEachIndexed { i, stress ->
                rect {
                    roundedCorners {
                        bottom = stress.low == 24
                        top = true
                        r = 2.dp
                    }

                    relative {
                        translate(40.vpX * i, 0f)
                        width = 16.vpX
                        height = 92.vpY * stress.low / 24
                    }

                    paint = graphLow
                }

                rect {
                    roundedCorners {
                        bottom = stress.low + stress.medium == 24
                        top = false
                        r = 2.dp
                    }

                    relative {
                        translate(40.vpX * i, 92.vpY * stress.low / 24f)
                        width = 16.vpX
                        height = 92.vpY * stress.medium / 24
                    }

                    paint = graphMedium
                }

                rect {
                    roundedCorners {
                        bottom = stress.high + stress.medium + stress.low == 24
                        top = false
                        r = 2.dp
                    }

                    relative {
                        translate(40.vpX * i, 92.vpY * stress.low / 24f + 92.vpY * stress.medium / 24f)
                        width = 16.vpX
                        height = 92.vpY * stress.high / 24
                    }

                    paint = graphHi
                }

                ref("weekArrow") {
                    relative {
                        translate(40.vpX * i + 16.vpX + 2.dp, 92.vpY * stress.average / 24f - 3.5f.dp)
                    }
                }
            }
        }
    }

    paintWeek(this@stressWeek, data.map { it.wear }.toTypedArray())
    paintStressPercents(this@stressWeek, data)
}


fun Drawing.paintStressPercents(context: Context, data: Array<DayStress>) = group {
    val textPercents by context.textPercents()
    val graphLow by context.stressLow()
    val graphMedium by context.stressMedium()
    val graphHi by context.stressHi()

    val hi = data.sumOf { it.high }
    val med = data.sumOf { it.medium }
    val low = data.sumOf { it.low }

    relative {
        translate(17.vpX, 0f)
    }

    text {
        paint = textPercents
        text = (100 - 100 * hi / (low + med + hi) - 100 * med / (low + med + hi)).toString() + "%"
        relative {
            translate(7.vpX, 108.vpY - 12.dp)
        }
    }

    text {
        paint = textPercents
        text = (100 * med / (low + med + hi)).toString() + "%"
        relative {
            translate(7.vpX, 108.vpY - 28.dp)
        }
    }

    text {
        paint = textPercents
        text = (100 * hi / (low + med + hi)).toString() + "%"
        relative {
            translate(7.vpX, 108.vpY - 44.dp)
        }
    }

    oval {
        width = 6.dp
        height = 6.dp

        relative {
            translate(7.vpX - 10.dp, 108.vpY - 10.dp)
        }

        paint = graphLow
    }

    oval {
        width = 6.dp
        height = 6.dp

        relative {
            translate(7.vpX - 10.dp, 108.vpY - 26.dp)
        }

        paint = graphMedium
    }

    oval {
        width = 6.dp
        height = 6.dp

        relative {
            translate(7.vpX - 10.dp, 108.vpY - 42.dp)
        }

        paint = graphHi
    }
}

