package com.mytoolbox.canvasdsl.example

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mytoolbox.example.R

class MainActivity : AppCompatActivity() {
    private val drawing by example()
    val stripesP by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = ContextCompat.getColor(applicationContext, R.color.light_gray_05)
        }
    }

    val graphP by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = ContextCompat.getColor(applicationContext, R.color.teal)
        }
    }

    val textP by lazy {
        Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(applicationContext, R.color.light_black_87)
            typeface = Typeface.DEFAULT
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.picture).setImageDrawable(drawing)
        Log.i("qwer", "wtf");
    }
}
