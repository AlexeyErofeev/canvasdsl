package com.mytoolbox.canvasdsl.example


import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mytoolbox.canvasdsl.common.dp
import com.mytoolbox.canvasdsl.drawing
import com.mytoolbox.canvasdsl.primitives.Group
import com.mytoolbox.canvasdsl.primitives.group

import com.mytoolbox.canvasdsl.primitives.path
import com.mytoolbox.canvasdsl.primitives.rect
import com.mytoolbox.canvasdsl.primitives.Path as DPath

import com.mytoolbox.canvasdsl.utils.groupFromXml

import com.mytoolbox.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val drawing by drawing {
        fitToHost = true

        group {
            id = "root"
            scale(15f, 15f)

            groupFromXml(R.drawable.week_arrow) {
                id = "play_drawable"
                pivot(2.5f.dp, 3.5f.dp)
                translate(5.dp, 0f)
                rotate(90f)
                rect {
                    translate(2.5f.dp, 3.5f.dp)
                    width = 1f
                    height = 1f
                    paint { color = Color.BLACK }
                }
            }

            group {
                translate(15.dp, 0f)
                rect {
                    width = 5.dp
                    height = 7.dp
                    paint { color = Color.BLACK }
                }

                rect {
                    translate(2.5f.dp, 3.5f.dp)
                    width = 1f
                    height = 1f
                    paint { color = Color.WHITE }
                }
            }
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
        val playGroup = drawing.rootNode.nodeById<Group>("play")
        val fillPath = drawing.rootNode.nodeById<DPath>("play_fill")
        var strokePath1: DPath? = null
        var strokePath2: DPath? = null

        playGroup.apply {
            path {
                strokePath1 = this
                id = "play_stroke1"
                path = fillPath.path
                paint {
                    style = Paint.Style.STROKE
                    strokeWidth = 1f
                    strokeCap = Paint.Cap.ROUND
                }
            }

            path {
                strokePath2 = this@path
                id = "play_stroke2"
                path = fillPath.path
                paint {
                    style = Paint.Style.STROKE
                    strokeWidth = 1f
                    strokeCap = Paint.Cap.ROUND
                }
            }
        }

        endlessAnimate {
            strokePath1?.trimPath(
                0f,
                .5f,
                it
            )

            strokePath2?.trimPath(
                0f,
                .5f,
                it - 1f
            )
        }
    }

    private fun endlessAnimate(block: (animationValue: Float) -> Unit) = apply {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            block(it.animatedValue as Float)
            drawing.invalidateSelf()
        }
        animator.start()
    }
}
