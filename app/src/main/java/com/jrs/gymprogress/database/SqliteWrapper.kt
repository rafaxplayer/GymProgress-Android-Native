package com.jrs.gymprogress.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.database.models.Training
import kotlin.Any as Any


class SqliteWrapper(val context: Context) {

    private val dbHelper: DBHelper = DBHelper(context)

    //Entrenos crud
    fun getTrainingsWithDate(date: String): ArrayList<Training> {
        val db: SQLiteDatabase? = dbHelper.readableDatabase
        var list = ArrayList<Training>()
        val query =
            "SELECT t.id, t.date, t.muscle_group_id, t.exercise_id, t.comment, t.series, e.name AS exercise_name FROM ${DBHelper.TABLE_TRAININGS} AS t " +
                    "LEFT JOIN ${DBHelper.TABLE_EXERCISES} AS e ON e.id = t.exercise_id " +
                    "WHERE ${DBHelper.DATE} = '$date' ORDER BY ${DBHelper.DATE};"

        val cursor: Cursor? = db!!.rawQuery(query, null)

        try {
            if (cursor!!.moveToFirst()) {
                do {
                    var trn = Training()
                    trn.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                    trn.date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE))
                    trn.muscle_group_id =
                        cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))
                    trn.exercise_id = cursor.getInt(cursor.getColumnIndex(DBHelper.EXERCISE_ID))
                    trn.comment = cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT))
                    trn.series = cursor.getString(cursor.getColumnIndex(DBHelper.SERIES))
                    trn.exercise_name = cursor.getString(cursor.getColumnIndex("exercise_name"))
                    list.add(trn)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
            if (db.isOpen) db.close()
        }
        return list
    }

    //Exercises crud
    fun getDataWitColumnValue(table: String, column: String, value: String): ArrayList<Any> {
        val db: SQLiteDatabase? = dbHelper.readableDatabase
        var cursor: Cursor? = null
        var list = ArrayList<Any>()
        try {
            when (table) {
                DBHelper.TABLE_TRAININGS -> {
                    cursor = db!!.rawQuery(
                        "SELECT t.id, t.date, t.muscle_group_id, t.exercise_id, t.comment, t.series, e.name AS exercise_name FROM ${DBHelper.TABLE_TRAININGS} AS t " +
                                "LEFT JOIN ${DBHelper.TABLE_EXERCISES} AS e ON e.id = t.exercise_id " +
                                "WHERE $column = '$value' ORDER BY ${DBHelper.DATE}",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var trn = Training()
                            trn.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            trn.date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE))
                            trn.muscle_group_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))
                            trn.exercise_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.EXERCISE_ID))
                            trn.series =
                                cursor.getString(cursor.getColumnIndex(DBHelper.SERIES))
                            trn.comment =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT))
                            trn.exercise_name = cursor.getString(cursor.getColumnIndex("exercise_name"))
                            list.add(trn)
                        } while (cursor.moveToNext())
                    }


                }
                DBHelper.TABLE_EXERCISES -> {
                    cursor = db!!.rawQuery(
                        "SELECT * FROM ${DBHelper.TABLE_EXERCISES} WHERE $column = '$value'",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var exer = Exercise()
                            exer.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            exer.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                            exer.muscle_group_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))

                            list.add(exer)
                        } while (cursor.moveToNext())
                    }

                }
                DBHelper.TABLE_MUSCLE_GROUPS -> {
                    cursor = db!!.rawQuery(
                        "SELECT * FROM ${DBHelper.TABLE_MUSCLE_GROUPS} WHERE $column = '$value'",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var mus = MuscleGroup()
                            mus.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            mus.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                            mus.img_path =
                                cursor.getString(cursor!!.getColumnIndex(DBHelper.IMG_PATH))
                            list.add(mus)
                        } while (cursor!!.moveToNext())
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db!!.close()
        }
        return list
    }

    // General Functions
    fun getAllData(table: String): ArrayList<Any> {
        val db: SQLiteDatabase? = dbHelper.readableDatabase
        var cursor: Cursor? = null
        var list = ArrayList<Any>()
        try {
            when (table) {
                DBHelper.TABLE_TRAININGS -> {
                    cursor = db!!.rawQuery(
                        "SELECT * FROM ${DBHelper.TABLE_TRAININGS} ORDER BY ${DBHelper.DATE}",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var trn = Training()
                            trn.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            trn.date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE))
                            trn.muscle_group_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))
                            trn.exercise_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.EXERCISE_ID))
                            trn.series =
                                cursor.getString(cursor.getColumnIndex(DBHelper.SERIES))
                            trn.comment =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT))

                            list.add(trn)
                        } while (cursor.moveToNext())
                    }


                }
                DBHelper.TABLE_EXERCISES -> {
                    cursor = db!!.rawQuery("SELECT * FROM ${DBHelper.TABLE_EXERCISES}", null)

                    if (cursor.moveToFirst()) {
                        do {
                            var exer = Exercise()
                            exer.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            exer.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                            exer.muscle_group_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))

                            list.add(exer)
                        } while (cursor.moveToNext())
                    }

                }
                DBHelper.TABLE_MUSCLE_GROUPS -> {
                    cursor = db!!.rawQuery("SELECT * FROM ${DBHelper.TABLE_MUSCLE_GROUPS}", null)

                    if (cursor.moveToFirst()) {
                        do {
                            var mus = MuscleGroup()
                            mus.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            mus.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                            mus.img_path =
                                cursor.getString(cursor!!.getColumnIndex(DBHelper.IMG_PATH))

                            list.add(mus)
                        } while (cursor!!.moveToNext())
                    }

                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()

        } finally {
            cursor?.close()
            db!!.close()
        }

        return list
    }

    fun checkIfDataExists(table: String, column: String, data: Any): Boolean {
        val db: SQLiteDatabase? = dbHelper.readableDatabase
        var cursor: Cursor? = null;
        return try {
            cursor = db!!.rawQuery(
                "SELECT $column FROM $table WHERE $column = $data COLLATE NOCASE",
                null
            )
            cursor.count > 0
        } catch (ex: SQLException) {
            ex.printStackTrace()
            false
        } finally {
            if (db!!.isOpen) db.close()
            cursor?.close()

        }


    }

    fun getDataWithId(table: String, id: Int): Any {
        val db: SQLiteDatabase? = dbHelper.readableDatabase
        var cursor: Cursor? = null;
        try {
            when (table) {
                DBHelper.TABLE_TRAININGS -> {
                    cursor = db!!.rawQuery(
                        "SELECT t.id, t.date, t.muscle_group_id, t.exercise_id, t.comment, t.series, e.name AS exercise_name FROM ${DBHelper.TABLE_TRAININGS} AS t " +
                                "LEFT JOIN ${DBHelper.TABLE_EXERCISES} AS e ON e.id = t.exercise_id " +
                                "WHERE t.${DBHelper.ID} = $id ORDER BY t.${DBHelper.DATE}",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var trn = Training()
                            trn.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            trn.date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE))
                            trn.muscle_group_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))
                            trn.exercise_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.EXERCISE_ID))
                            trn.series =
                                cursor.getString(cursor.getColumnIndex(DBHelper.SERIES))
                            trn.comment =
                                cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT))
                            trn.exercise_name = cursor.getString(cursor.getColumnIndex("exercise_name"))
                            return trn;
                        } while (cursor.moveToNext())
                    }


                }
                DBHelper.TABLE_EXERCISES -> {
                    cursor = db!!.rawQuery(
                        "SELECT * FROM ${DBHelper.TABLE_EXERCISES} WHERE ${DBHelper.ID} = $id",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var exer = Exercise()
                            exer.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            exer.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                            exer.muscle_group_id =
                                cursor.getInt(cursor.getColumnIndex(DBHelper.MUSCLE_GROUP_ID))

                            return exer
                        } while (cursor.moveToNext())
                    }

                }
                DBHelper.TABLE_MUSCLE_GROUPS -> {
                    cursor = db!!.rawQuery(
                        "SELECT * FROM ${DBHelper.TABLE_MUSCLE_GROUPS} WHERE ${DBHelper.ID} = $id",
                        null
                    )

                    if (cursor.moveToFirst()) {
                        do {
                            var mus = MuscleGroup()
                            mus.id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID))
                            mus.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME))
                            mus.img_path =
                                cursor.getString(cursor.getColumnIndex(DBHelper.IMG_PATH))

                            return mus
                        } while (cursor.moveToNext())
                    }

                }

            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            if (db!!.isOpen) db.close()
        }
        return 0
    }

    fun insertData(ob: Any): Long {
        var id: Long = -1
        val db: SQLiteDatabase? = dbHelper.writableDatabase
        val values = ContentValues()
        try {
            when (ob) {
                is Training -> {
                    values.put(DBHelper.DATE, ob.date)
                    values.put(DBHelper.MUSCLE_GROUP_ID, ob.muscle_group_id)
                    values.put(DBHelper.EXERCISE_ID, ob.exercise_id)
                    values.put(DBHelper.COMMENT, ob.comment)
                    values.put(DBHelper.SERIES, ob.series)
                    id = db!!.insert(DBHelper.TABLE_TRAININGS, null, values)
                }
                is MuscleGroup -> {
                    values.put(DBHelper.NAME, ob.name)
                    values.put(DBHelper.IMG_PATH, ob.img_path)
                    id = db!!.insert(DBHelper.TABLE_MUSCLE_GROUPS, null, values)
                }
                is Exercise -> {
                    values.put(DBHelper.NAME, ob.name)
                    values.put(DBHelper.MUSCLE_GROUP_ID, ob.muscle_group_id)
                    id = db!!.insert(DBHelper.TABLE_EXERCISES, null, values)
                }
            }
            values.clear()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        if (db!!.isOpen) db.close();
        return id
    }

    fun deleteDataWithId(table: String, id: Int): Boolean {
        val db: SQLiteDatabase? = dbHelper.writableDatabase
        var ret: Int
        try {
            ret = db!!.delete(table, DBHelper.ID + " = ?", arrayOf(id.toString()))
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        } finally {
            if (db!!.isOpen) db.close();
        }
        return ret > 0
    }

    fun updateDataWithId(ob: Any): Boolean {

        val db: SQLiteDatabase? = dbHelper.writableDatabase
        val values = ContentValues()
        try {
            when (ob) {
                is Training -> {
                    values.put(DBHelper.DATE, ob.date)
                    values.put(DBHelper.MUSCLE_GROUP_ID, ob.muscle_group_id)
                    values.put(DBHelper.EXERCISE_ID, ob.exercise_id)
                    values.put(DBHelper.COMMENT, ob.comment)
                    values.put(DBHelper.SERIES, ob.series)
                    db!!.update(
                        DBHelper.TABLE_TRAININGS,
                        values,
                        DBHelper.ID + " = ?",
                        arrayOf(ob.id.toString())
                    )
                    return true
                }
                is MuscleGroup -> {
                    values.put(DBHelper.NAME, ob.name)
                    values.put(DBHelper.IMG_PATH, ob.img_path)
                    db!!.update(
                        DBHelper.TABLE_MUSCLE_GROUPS,
                        values,
                        DBHelper.ID + " = ?",
                        arrayOf(ob.id.toString())
                    )
                    return true
                }
                is Exercise -> {
                    values.put(DBHelper.NAME, ob.name)
                    values.put(DBHelper.MUSCLE_GROUP_ID, ob.muscle_group_id)
                    db!!.update(
                        DBHelper.TABLE_EXERCISES,
                        values,
                        DBHelper.ID + " = ?",
                        arrayOf(ob.id.toString())
                    )
                    return true
                }
            }
            values.clear()
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        } finally {
            if (db!!.isOpen) db.close();
        }
        return false
    }
}