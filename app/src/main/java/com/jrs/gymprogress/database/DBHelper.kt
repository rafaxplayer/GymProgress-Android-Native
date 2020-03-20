package com.jrs.gymprogress.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {

    companion object {
        //database name
        private val DATABASE_NAME = "gym_progress_android"

        //database version
        private val DATABASE_VERSION = 1

        //Tablas nombres...
        val TABLE_TRAININGS = "trainings"
        val TABLE_MUSCLE_GROUPS = "muscle_groups"
        val TABLE_EXERCISES = "exercises"

        //Campos generales...
        val ID = "id"
        val NAME = "name"
        val DATE = "date"
        val COMMENT = "comment"
        val SERIES = "series"
        val MUSCLE_GROUP_ID = "muscle_group_id"
        val EXERCISE_ID = "exercise_id"
        val IMG_PATH = "img_path"

        private val SqlCreateTable_trainings: String =
            "CREATE TABLE IF NOT EXISTS $TABLE_TRAININGS ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $DATE TEXT, $MUSCLE_GROUP_ID INTEGER, $EXERCISE_ID INTEGER, $SERIES TEXT, $COMMENT TEXT )"

        private val SqlCreateTable_muscle_groups: String =
            "CREATE TABLE IF NOT EXISTS $TABLE_MUSCLE_GROUPS ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $IMG_PATH TEXT)"

        private val SqlCreateTable_exercises: String =
            "CREATE TABLE IF NOT EXISTS $TABLE_EXERCISES ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $MUSCLE_GROUP_ID INTEGER)"

        private val SqlCreateTrigger_onDelete_Muscle_group: String =
            "CREATE TRIGGER IF NOT EXISTS delete_muscle_group AFTER DELETE ON $TABLE_MUSCLE_GROUPS FOR EACH ROW BEGIN DELETE FROM $TABLE_EXERCISES WHERE $MUSCLE_GROUP_ID = OLD.id; " +
                    "DELETE FROM $TABLE_TRAININGS WHERE trainings.muscle_group_id = OLD.id;END; DELETE FROM $TABLE_EXERCISES WHERE exercises.muscle_group_id = OLD.id;END;"
    }


    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(SqlCreateTable_trainings)
        db.execSQL(SqlCreateTable_muscle_groups)
        db.execSQL(SqlCreateTable_exercises)
        db.execSQL(SqlCreateTrigger_onDelete_Muscle_group)
        val arrayMuscleGroups =
            arrayOf("Pecho", "Espalda", "Hombros", "Piernas", "Biceps", "Triceps")

        arrayMuscleGroups.forEach {
            val values = ContentValues()
            values.put(NAME, it)
            values.put(IMG_PATH,"")
            db.insert(TABLE_MUSCLE_GROUPS,null, values)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TRAININGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MUSCLE_GROUPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISES")
        onCreate(db)
    }



}