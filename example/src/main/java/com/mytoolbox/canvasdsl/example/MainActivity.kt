package com.mytoolbox.canvasdsl.example

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mytoolbox.canvasdsl.example.chart.DayStress
import com.mytoolbox.canvasdsl.example.chart.stressWeek
import com.mytoolbox.example.R

class MainActivity : AppCompatActivity() {
    private val dataStress = arrayOf(
        DayStress(0, 0, 0, false, 1),
        DayStress(1, 0, 0, false, 1),
        DayStress(6, 4, 2, true, 6),
        DayStress(16, 6, 2, true, 20),
        DayStress(10, 6, 2, true, 12),
        DayStress(1, 0, 0, false, 1),
        DayStress(15, 9, 0, true, 12)
    )

    //simplified chart bar from applied project
    private val drawing by stressWeek(dataStress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.picture).setImageDrawable(drawing)
    }
}
