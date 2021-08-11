package com.practice.astronomy.viewmodel

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.android.volley.VolleyError
import org.json.JSONArray


class AstroRepository() {

    private val url = "https://raw.githubusercontent.com/cmmobile/NasaDataSet/main/apod.json"

    fun loading(context: Context, dataValues: DataValues) {
        val queue = Volley.newRequestQueue(context)
        val jsonRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                dataValues.setJsonDataResponse(response)
            },
            Response.ErrorListener {
                dataValues.setVolleyError(it)
            })
        queue.add(jsonRequest)

    }


    interface DataValues {
        fun setJsonDataResponse(response: JSONArray?)
        fun setVolleyError(volleyError: VolleyError?)
    }

}