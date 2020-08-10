package com.mytoolbox.canvasdsl.example.chart

import android.annotation.SuppressLint
import android.content.Context
import com.mytoolbox.canvasdsl.Drawing
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.primitives.Group
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.canvasdsl.primitives.rect
import com.mytoolbox.canvasdsl.primitives.text
import java.text.SimpleDateFormat
import java.util.*


fun Group.paintGrayBars(context: Context) = group {
    val stripesP by context.stripesP()

    for (i in 0..6) {
        rect {
            roundedCorners {
                all = true
                r = 2.dp
            }

            relative {
                translate(40.vpX * i, 0f)
                width = 16.vpX
                height = 92.vpY
            }

            paint = stripesP
        }
    }
}

fun formatDayOfWeek(date: Date): String = SimpleDateFormat("EE", Locale.ENGLISH).format(date)

@SuppressLint("DefaultLocale")
fun Drawing.paintWeek(context: Context, wear: Array<Boolean>) = group {
    val noColor by context.noColor()
    val textWN by context.textWN()
    val textWY by context.textWY()
    val stripesP by context.stripesP()

    val times = Calendar.getInstance().run {
        return@run Array<Date>(7) {
            this.apply { add(Calendar.HOUR, -24) }.time
        }
    }

    relative {
        translate(52.vpX, 118.vpY)
    }

    for (i in 0..6) {
        rect {
            roundedCorners {
                all = true
                r = 2.dp
            }

            relative {
                translate(40.vpX * i, 0f)
                width = 24.vpX
                height = 16.vpY
            }

            paint = if (wear[i]) noColor else stripesP
        }

        text {
            paint = if (wear[i]) textWY else textWN
            text = formatDayOfWeek(times[6 - i]).capitalize()

            relative {
                translate(40.vpX * i + 12.vpX - size.width / 2, 8.vpY - size.height / 2)
            }
        }
    }
}