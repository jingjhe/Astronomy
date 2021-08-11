package com.practice.astronomy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.gson.Gson
import com.practice.astronomy.R
import com.practice.astronomy.model.Astronomy
import com.practice.astronomy.viewmodel.AstroViewModel

class ListAstronomyActivity : AppCompatActivity(),OnItemActionListener {

    private lateinit var astroViewModel: AstroViewModel
    private var astroAdapter = AstroAdapter()
    private lateinit var recyclerView: RecyclerView

    companion object{
        const val EXTRA_MESSAGE="objString"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_astronomy)
        initView()
        astroViewModel = ViewModelProvider(this).get(AstroViewModel::class.java)
        astroViewModel.fetchData(this.applicationContext)

        astroViewModel.liveDataAstro.observe(this, Observer {
            astroAdapter.setAstroList(it)
            astroAdapter.notifyDataSetChanged()

        })

        astroViewModel.liveDataErrorMsg.observe(this, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        })

    }

    private fun initView() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@ListAstronomyActivity,4,LinearLayoutManager.VERTICAL, false)
            val decoration =
                DividerItemDecoration(this@ListAstronomyActivity, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(decoration)
            astroAdapter.setListener(this@ListAstronomyActivity)
            adapter = astroAdapter

        }

    }
    override fun onClick(astronomy: Astronomy) {
        val  message= Gson().toJson(astronomy).toString()
        val intent = Intent(this, AstroDetailActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

}