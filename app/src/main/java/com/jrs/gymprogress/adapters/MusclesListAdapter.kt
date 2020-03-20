package com.jrs.gymprogress.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.RecyclerView
import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.fragments.NewMuscleGroupFragment
import kotlinx.android.synthetic.main.list_item.view.*

class MusclesListAdapter(
    val context: Context,
    val list: ArrayList<MuscleGroup>,
    val db: SqliteWrapper
) : RecyclerView.Adapter<MusclesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    fun deleteItem(position: Int) {
        var ret = db.deleteDataWithId(DBHelper.TABLE_MUSCLE_GROUPS, list[position].id)
        var name = list[position].name
        if (ret) {
            list.remove(list[position]);
            Toast.makeText(
                context,
                "Ok,entrada ( ${name} ) eliminada con exito",
                Toast.LENGTH_LONG
            ).show()
            notifyItemRemoved(position)
        } else {
            Toast.makeText(
                context,
                "Error:, Ocurrio un error al eliminar ${name}",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("RestrictedApi")
        fun bindItems(item: MuscleGroup) {

            itemView.txtMuscleItem.text = item.name

            itemView.btnItemListOptions.setOnClickListener {

                var menuBuilder: MenuBuilder = MenuBuilder(context)
                var inflater: MenuInflater = MenuInflater(context)
                inflater.inflate(R.menu.popup_lists_items, menuBuilder)
                var optionsMenu = MenuPopupHelper(context, menuBuilder, itemView.btnItemListOptions)
                optionsMenu.setForceShowIcon(true)
                menuBuilder.setCallback(object : MenuBuilder.Callback {
                    override fun onMenuModeChange(menu: MenuBuilder?) {
                        TODO("Not yet implemented")
                    }

                    override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
                        when (item?.itemId) {
                            R.id.action_edit -> {
                                val transaction =
                                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                val newMuscleDialog =
                                    NewMuscleGroupFragment.newInstance(list[layoutPosition].id)
                                newMuscleDialog.show(transaction, Utils.DIALOG_NEW_EDIT_EXERCISE)
                            }
                            R.id.action_delete -> deleteItem(layoutPosition)
                        }
                        return true
                    }

                })
                optionsMenu.show()

            }
        }

    }

}