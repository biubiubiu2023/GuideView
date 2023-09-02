package com.example.guideview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val guideView = findViewById<MainGuideView>(R.id.main_guide_view)
        guideView.startGuideView()
    }
}