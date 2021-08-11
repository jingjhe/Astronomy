package com.practice.astronomy.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.VolleyError
import com.practice.astronomy.model.Astronomy
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class AstroViewModel : ViewModel() {

    private val TAG by lazy { this.javaClass.simpleName }
    private val repository: AstroRepository = AstroRepository()


    companion object {
        fun downloadBitmap(imageUrl: String): Bitmap? {
            return try {
                val conn = URL(imageUrl).openConnection()
                conn.connect()
                val inputStream = conn.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                bitmap
            } catch (e: Exception) {
                Log.e("AstroViewModel", "downloadBitmap_Error_$e")
                null
            }
        }
    }

    val liveDataAstro: MutableLiveData<ArrayList<Astronomy>> by lazy {
        MutableLiveData<ArrayList<Astronomy>>()
    }

    val liveDataErrorMsg: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun fetchData(context: Context) {
        repository.loading(context, object : AstroRepository.DataValues {
            override fun setJsonDataResponse(response: JSONArray?) {
                if (response != null) {
                    val list = arrayListOf<Astronomy>()
                    list.clear()
                    for (i in 0 until response.length()) {
                        list.add(convertAstronomy(response.getJSONObject(i)))
                    }
                    liveDataAstro.postValue(list)
                } else {
                    liveDataErrorMsg.postValue("null")
                }
            }

            override fun setVolleyError(volleyError: VolleyError?) {
                liveDataErrorMsg.postValue(volleyError!!.message)
            }

        })
    }


    fun convertAstronomy(jsonObject: JSONObject): Astronomy {
        val astronomy = Astronomy()
        astronomy.description = jsonObject.optString("description")
        astronomy.url = jsonObject.optString("url")
        astronomy.date = jsonObject.optString("date")
        astronomy.copyright = jsonObject.optString("copyright")
        astronomy.title = jsonObject.optString("title")
        return astronomy
    }

    fun dateFormat(string: String): String {
        return try {
            val fromFormat = SimpleDateFormat("yyyy-MM-dd")
            val toFormat= SimpleDateFormat("yyyy MMM.dd")
            val dateObj = fromFormat.parse(string)
            return toFormat.format(dateObj)
        } catch (e: Exception) {
            string+"$e"
        }
    }

}