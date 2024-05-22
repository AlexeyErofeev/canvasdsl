package com.mytoolbox.canvasdsl.example

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.canvasdsl.example.elements.*
import com.mytoolbox.canvasdsl.primitives.group
import com.mytoolbox.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    // fill our drawing with primitives which render themselves in onDrawing
    // drawing construction can work in any threadPool
    private val drawing by drawing {
        fitToHost = true

        // viewport parameters as in svg
        // let's take drawing viewport points as percents (viewport width and height are 1f by default)
        // then you draw for example 24h chart you can take it in hours, or in minutes
        viewport {
            width = 100f
            height = 100f
        }

        caption("Drawables") {
            relative {
                // 50.vpX is a center of drawing by x-axis, vpX/vpY is only available in "relative" section
                // text is measurable, using "size" we can center it in drawing area
                // it works like (screenWidth - elementWidth) / 2 or screenCenter - elementWidth / 2
                translate(50.vpX - size.width / 2, (48.dp - size.height) / 2)
            }
        }

        group {
            relative { translate(50.vpX - 144.dp, 48.dp) }
            // using scale we manipulate inner screen points without changes as on canvas
            // here we have 2x so icons is 80x80 but inside we use own icon sizes 40x40
            // but groupWidth in translation doubled for centering doubled group elements
            scale(2f, 2f)

            heart() // convert drawable to primitives
            sun { translateByX(48.dp) } // using as android drawable
            ball { translateByX(96.dp) } // using as android drawable
        }

        caption("Path") {
            relative { translate(50.vpX - size.width / 2, 156.dp + (48.dp - size.height) / 2) }
        }

        group {
            relative { translate(50.vpX - 81.dp, 232.dp) }
            infinity() // draw path from pathData
        }

        caption("Chart") {
            relative { translate(50.vpX - size.width / 2, 340.dp + (48.dp - size.height) / 2) }
        }

        group {
            relative { translate(50.vpX - 83.dp, 400.dp) }
            chart() // draw boxes
        }
    }

    private val image by lazy { findViewById<ImageView>(R.id.picture) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        val flow = MutableStateFlow(true)
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            flow.emit(false)
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition flow.value
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image.setImageDrawable(drawing)
    }

    override fun onResume() {
        super.onResume()

        drawing.apply {
            animateHeart()
            animateSun()
            animateBall()
            animateInfinity()
            animateChart()
        }
    }
}
