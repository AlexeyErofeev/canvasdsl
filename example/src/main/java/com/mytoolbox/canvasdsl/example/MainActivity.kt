package com.mytoolbox.canvasdsl.example

import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.common.color
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.example.R

class MainActivity : AppCompatActivity() {
    private val picture by drawing {
        viewport {
            width = 100f
            height = 100f
            margin = 16.dp
        }

        rect {
            roundedCorners {
                r = 4.dp
                all = true
            }

            relative {
                width = 50.vpX
                height = 50.vpY

                translate(25.vpX, 25.vpY)
            }

            paint {
                isAntiAlias = true
                color = color(R.color.colorPrimary)
                style = Paint.Style.FILL
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.picture).setImageDrawable(picture)
    }
}
