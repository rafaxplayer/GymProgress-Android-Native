package com.jrs.gymprogress.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.jrs.gymprogress.R
import com.jrs.gymprogress.database.models.Serie
import kotlinx.android.synthetic.main.list_series_item.view.*

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    var list: ArrayList<Serie>?

    constructor(list: ArrayList<Serie>?) : super() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_series_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = list!![position]
        holder.kilos?.setText(item.kilos.toString())
        holder.repes?.setText(item.repes.toString())
        holder.repes?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    item.apply {
                        repes=s.toString().toInt()
                    }


                }
            }
        })
        holder.kilos?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {

                    item.apply {
                        kilos=s.toString().toInt()
                    }

                }
            }

        })
        holder.btnDelete?.setOnClickListener { removeSerie(position) }
    }

    override fun getItemCount(): Int {
        return list!!.size;
    }

    fun removeSerie(position: Int) {
        list?.remove(list!![position])
        notifyDataSetChanged()

    }

    fun clearData() {
        list = arrayListOf();
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var kilos: EditText? = null
        var repes: EditText? = null
        var btnDelete: ImageButton? = null

        init {
            kilos = itemView.editKilos
            repes = itemView.editRepes
            btnDelete = itemView.deleteSerie

        }

    }


}


