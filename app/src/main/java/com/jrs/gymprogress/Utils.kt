package com.jrs.gymprogress

import android.app.Activity
import android.os.Handler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.database.models.Serie
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class Utils {

    companion object {

        val DIALOG_DATE_PICKER: String="date_picker"
        val DIALOG_COMPARE_TRAINIGS: String?= "compare_trainings"
        val DIALOG_NEW_EDIT_MUSCLE_GROUP: String= "new_edit_muscle_group"
        val DIALOG_NEW_EDIT_EXERCISE: String = "new_edit_exercise"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatEsp = SimpleDateFormat("dd-MM-yyyy")

        fun convertTimeToISODate(time: Long): String {
            return dateFormat.format(time)
        }

        fun reformatDate(s:String):String{
            val dat:Date? = dateFormat.parse(s)
            return dateFormatEsp.format(dat!!)
        }

        fun Int.twoDigits()= if(this<=9) "0$this" else this.toString()

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
    }
}