package com.practice.astronomy.view

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practice.astronomy.R
import com.practice.astronomy.model.Astronomy
import com.practice.astronomy.viewmodel.AstroViewModel
import kotlin.concurrent.thread

class AstroAdapter : RecyclerView.Adapter<AstroAdapter.ViewHold>() {
    private val TAG by lazy { this.javaClass.simpleName }

    private var astroList = ArrayList<Astronomy>()
    private var listener:OnItemActionListener?=null
    private var uiHandler = Handler(Looper.getMainLooper())

    class ViewHold(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)

        val textView: TextView = view.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_astro, viewGroup, false)

        return ViewHold(view)

    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.textView.text = astroList[position].title
        thread(start = true){
            val bitmap= AstroViewModel.downloadBitmap(astroList[position].url)
            uiHandler.post {
                holder.imageView.setImageBitmap(bitmap)
            }
        }
        if (listener!=null){
            holder.itemView.setOnClickListener {
                listener!!.onClick(astroList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return astroList.size
    }

    fun setAstroList(list: ArrayList<Astronomy>) {
        astroList = list
    }

    fun setListener(listener: OnItemActionListener){
        this.listener=listener
    }

}


interface OnItemActionListener {
    fun onClick(astronomy: Astronomy)
}