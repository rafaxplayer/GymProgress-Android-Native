package com.jrs.gymprogress

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.database.models.Serie
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        val DIALOG_DATE_PICKER: String = "date_picker"
        val DIALOG_COMPARE_TRAINIGS: String? = "compare_trainings"
        val DIALOG_NEW_EDIT_MUSCLE_GROUP: String = "new_edit_muscle_group"
        val DIALOG_NEW_EDIT_EXERCISE: String = "new_edit_exercise"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatEsp = SimpleDateFormat("dd-MM-yyyy")

        fun convertTimeToISODate(time: Long): String {
            return dateFormat.format(time)
        }

        fun reformatDate(s: String): String {
            val dat: Date? = dateFormat.parse(s)
            return dateFormatEsp.format(dat!!)
        }

        private fun Int.twoDigits() = if (this <= 9) "0$this" else this.toString()

        fun formatPartialsDate(year: Int, month: Int, day: Int): String {

            return "${year}-${month.twoDigits()}-${day.twoDigits()}"
        }

        fun arrayListSeriesToJson(list: ArrayList<Serie>): String? {
            var gson = Gson()
            return gson.toJson(list)

        }

        fun seriesToarrayList(json: String): ArrayList<Serie>? {
            var gson = Gson()
            var sType = object : TypeToken<ArrayList<Serie>>() {}.type
            return gson.fromJson<ArrayList<Serie>>(json, sType)
        }

        fun runAfterOnTime(delay: Long, process: () -> Unit) {
            Handler().postDelayed({
                process()
            }, delay)
        }

        fun <T> getPositionWithId(list: ArrayList<T>, id: Int): Int {

            for ((index, item) in list.withIndex()) {
                if (item is MuscleGroup) {
                    if (item.id == id) {
                        return index
                    }
                }
                if (item is Exercise) {
                    if (item.id == id) {
                        return index
                    }
                }
            }
            return 0
        }

        fun setSeriesToTableView(context: Context, table: TableLayout?, list: ArrayList<Serie>) {

            list.forEach { serie: Serie ->
                val row = TableRow(context)
                val txtKilos = TextView(context)
                txtKilos.apply {
                    layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                    text = serie.kilos.toString()
                    setTextColor(resources.getColor(R.color.textColorDark))
                    setPadding(15, 8, 8, 8)
                }
                val txtRepes = TextView(context)
                txtRepes.apply {
                    layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                    text = serie.repes.toString()
                    setTextColor(resources.getColor(R.color.textColorDark))

                    setPadding(15, 8, 8, 8)
                }
                row.addView(txtKilos)
                row.addView(txtRepes)
                table?.addView(row)
            }
        }

        fun dialog(
            activity: AppCompatActivity,
            title: String,
            message: String,
            positive: () -> Unit,
            negative: () -> Unit
        ): AlertDialog.Builder {
            val builder = AlertDialog.Builder(activity)
            builder.apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(android.R.string.yes) { dialog, which -> positive() }
                setNegativeButton(android.R.string.no) { dialog, which -> negative() }
            }

            return builder
        }
    }
}
