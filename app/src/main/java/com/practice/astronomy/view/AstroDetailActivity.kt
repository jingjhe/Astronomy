package com.practice.astronomy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.practice.astronomy.R
import com.practice.astronomy.model.Astronomy
import com.practice.astronomy.view.ListAstronomyActivity.Companion.EXTRA_MESSAGE
import com.practice.astronomy.viewmodel.AstroViewModel
import kotlin.concurrent.thread

class AstroDetailActivity : AppCompatActivity() {
    lateinit var dateText: TextView
    lateinit var contextTextView: TextView
    lateinit var imageView: ImageView
    var astoString: String? = null
    var astronomy: Astronomy? = null
    private lateinit var astroViewModel: AstroViewModel
    private var uiHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astro_detail)
        astoString = intent.getStringExtra(EXTRA_MESSAGE)
        if (!astoString.isNullOrEmpty()) {
            astroViewModel = ViewModelProvider(this).get(AstroViewModel::class.java)
            initView()
            loadData()
        } else {
            finish()
        }
    }

    private fun initView() {
        dateText = findViewById<TextView>(R.id.tvDate)
        contextTextView = findViewById(R.id.textContext)
        imageView = findViewById(R.id.imageView)
    }

    private fun loadData() {
        astronomy = Gson().fromJson(astoString, Astronomy::class.java)
        if (astronomy != null) {

            thread(start = true) {
                val bitmap = AstroViewModel.downloadBitmap(astronomy!!.url)
                uiHandler.post {
                    imageView.setImageBitmap(bitmap)
                }
            }
            dateText.text = astroViewModel.dateFormat(astronomy!!.date)
            contextTextView.text = "${astronomy!!.title}\n" +
                    "${astronomy!!.copyright}\n" +
                    "${astronomy!!.description}"

        }

    }

}