package com.practice.astronomy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.practice.astronomy.R

class MainActivity : AppCompatActivity() {

    private val requestTextView: TextView by lazy { findViewById<TextView>(R.id.tvRequest) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestTextView.setOnClickListener {
            startActivity(Intent(this,ListAstronomyActivity::class.java))
        }

    }
}