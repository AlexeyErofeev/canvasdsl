package com.mytoolbox.canvasdsl.example.chart

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import com.mytoolbox.example.R
import com.mytoolbox.canvasdsl.common.color
import com.mytoolbox.canvasdsl.common.dp

fun Context.stripesP() = lazy {
    Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color(R.color.light_gray_05)
    }
}

fun Context.noColor() = lazy {
    Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color(R.color.main_white_00)
    }
}

fun Context.stressLow() = lazy {
    Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color(R.color.normal_level)
    }
}

fun Context.stressMedium() = lazy {
    Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color(R.color.high_level)
    }
}

fun Context.stressHi() = lazy {
    Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color(R.color.danger_level)
    }
}


fun Context.textP() = lazy {
    Paint().apply {
        isAntiAlias = true
        color = color(R.color.light_black_87)
        typeface = Typeface.DEFAULT
        textSize = 10.dp
    }
}

fun Context.textWY() = lazy {
    Paint().apply {
        isAntiAlias = true
        color = color(R.color.light_black_87)
        typeface = Typeface.DEFAULT
        textSize = 9.dp
    }
}

fun Context.textWN() = lazy {
    Paint().apply {
        isAntiAlias = true
        color = color(R.color.not_wear_day)
        typeface = Typeface.DEFAULT
        textSize = 9.dp
    }
}

fun Context.textPercents() = lazy {
    Paint().apply {
        isAntiAlias = true
        color = color(R.color.light_black_87)
        typeface = Typeface.DEFAULT_BOLD
        textSize = 12.dp
    }
}