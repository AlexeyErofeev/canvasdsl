package com.mytoolbox.canvasdsl.example

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.common.color
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.example.R

class ImageHolder(context: Context) {
    val image by context.image()

    private fun Context.image() = drawing {
        viewport {
            width = 100f
            height = 100f
            margin = 16.dp
        }

        rect {
            roundedCorners { r = 4.dp }

            relative {
                translate(25.vpX, 25.vpY)

                width = 50.vpX
                height = 50.vpY
            }

            paint {
                isAntiAlias = true
                color = color(R.color.colorPrimary)
                style = Paint.Style.FILL
            }
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.picture).setImageDrawable(ImageHolder(this).image)
    }
}
