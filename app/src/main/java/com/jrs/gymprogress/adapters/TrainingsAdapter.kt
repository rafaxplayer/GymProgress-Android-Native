package com.jrs.gymprogress.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.RecyclerView
import com.jrs.gymprogress.*
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Training

import kotlinx.android.synthetic.main.list_entrenos_card.view.*

class TrainingsAdapter(var context:Context, private val list: ArrayList<Training>, val db:SqliteWrapper) :
    RecyclerView.Adapter<TrainingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_entrenos_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    fun deleteEntreno(position:Int){

        var ret=db.deleteDataWithId(DBHelper.TABLE_TRAININGS,list[position].id)
        if(ret){
            list.removeAt(position)
            Toast.makeText(context,"Ok, Entreno Eliminado!!!",Toast.LENGTH_LONG).show()
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        fun bindItems(item: Training) {

            itemView.textEntrenoName.text = item.exercise_name
            itemView.textDate.text = Utils.reformatDate(item.date)
            itemView.textComment.text = item.comment
            var table = itemView.tableSeries
            var series = Utils.seriesToarrayList(item.series)
            series.let {
                Utils.setSeriesToTableView(context, table, it!!)
            }
            itemView.btnEditEntreno.setOnClickListener(this)
            itemView.btnDeleteEntreno.setOnClickListener(this)
            itemView.btnCompareEntreno.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var training = list[layoutPosition]
            when (v!!.id) {
                R.id.btnDeleteEntreno -> {
                    Utils.dialog(
                        context as AppCompatActivity,
                        "Eliminar",
                        "¿Seguro?",
                        { deleteEntreno(layoutPosition) },
                        {}).show()
                }
                R.id.btnEditEntreno -> {
                    val intent = Intent(v.context, TrainingsActivity::class.java)
                    intent.putExtra("date", training.date)
                    intent.putExtra("entreno_id", training.id)
                    (v.context as GymProgressActivity).startActivity(intent)
                }
                R.id.btnCompareEntreno -> {
                    val intent = Intent(context, CompareDatesActivity::class.java)
                    intent.putExtra("training_id", training.id)
                    intent.putExtra("exercise_id", training.exercise_id)
                    (context as AppCompatActivity).startActivity(intent)

                }
            }
        }
    }
}