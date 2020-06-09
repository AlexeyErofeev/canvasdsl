package com.mytoolbox.canvasdsl.example

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mytoolbox.example.R

class MainActivity : AppCompatActivity() {
    private val drawing by example()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageView>(R.id.picture).setImageDrawable(drawing)
    }
}
