package com.jrs.gymprogress

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrs.gymprogress.adapters.SeriesAdapter
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.database.models.Serie
import com.jrs.gymprogress.database.models.Training
import com.jrs.gymprogress.fragments.DatePickerFragment
import com.jrs.gymprogress.fragments.NewExerciseFragment
import com.jrs.gymprogress.fragments.NewMuscleGroupFragment
import com.jrs.gymprogress.interfaces.DatePickerChangeListener
import com.jrs.gymprogress.interfaces.SaveDataListener
import kotlinx.android.synthetic.main.activity_training.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class TrainingsActivity : AppCompatActivity(), SaveDataListener, DatePickerChangeListener {

    var db: SqliteWrapper? = null
    var id: Int? = 0
    var date: String? = Utils.convertTimeToISODate(Calendar.getInstance().timeInMillis)
    var muscle_id: Int? = 0
    var exercise_id: Int? = 0
    var series: String? = ""
    var listMuscles: ArrayList<MuscleGroup>? = null
    var listExercises: ArrayList<Exercise>? = null
    var listSeries: ArrayList<Serie> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        setSupportActionBar(toolbar)

        db = SqliteWrapper(applicationContext)


        spinnerGroups.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                muscle_id = listMuscles!![position].id
                setSpinnerData(DBHelper.TABLE_EXERCISES, muscle_id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@TrainingsActivity, "Nothing", Toast.LENGTH_SHORT).show()
            }
        }

        spinnerExercises.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                exercise_id = listExercises!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@TrainingsActivity, "Nothing", Toast.LENGTH_SHORT).show()
            }
        }

        textDate.setOnClickListener { showDatePicker() }

        date = intent.getStringExtra("date")
        id = intent.getIntExtra("entreno_id", 0)
        textDate.text = date
        listSeriesRecycler.layoutManager = LinearLayoutManager(this)
        listSeriesRecycler.adapter = SeriesAdapter(listSeries)


    }

    override fun onResume() {
        super.onResume()
        setSpinnerData(DBHelper.TABLE_MUSCLE_GROUPS, muscle_id!!)
        setData(this.id!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(this@TrainingsActivity, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun setSpinnerData(tableName: String, muscle_id: Int) {

        when (tableName) {
            DBHelper.TABLE_MUSCLE_GROUPS -> {
                listMuscles =
                    db!!.getAllData(DBHelper.TABLE_MUSCLE_GROUPS) as ArrayList<MuscleGroup>
                val arrayMuscles = listMuscles!!.map { it.name }
                val arrayAdapter =
                    ArrayAdapter(this@TrainingsActivity, R.layout.simple_spinner_item, arrayMuscles)
                spinnerGroups.adapter = arrayAdapter
            }
            DBHelper.TABLE_EXERCISES -> {

                listExercises = db!!.getDataWitColumnValue(
                    DBHelper.TABLE_EXERCISES,
                    DBHelper.MUSCLE_GROUP_ID,
                    muscle_id.toString()
                ) as ArrayList<Exercise>
                listExercises.let {
                    val arrayExercises: List<String>? = listExercises!!.map { it.name }
                    val arrayAdapter2: ArrayAdapter<String>? =
                        ArrayAdapter(
                            this@TrainingsActivity,
                            R.layout.simple_spinner_item,
                            arrayExercises!!
                        )

                    spinnerExercises!!.adapter = arrayAdapter2
                }

            }
        }
    }

    fun addSerie(view: View) {
        val ser = Serie()
        listSeries.add(ser)
        listSeriesRecycler.adapter!!.notifyDataSetChanged()
        scrollEntrenos.post {
            scrollEntrenos.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun showDatePicker() {
        val transaction = supportFragmentManager.beginTransaction()
        val dateDialog = date?.let { DatePickerFragment.newInstance(it) }
        dateDialog?.show(transaction, Utils.DIALOG_DATE_PICKER)
    }

    private fun setData(entreno_id: Int) {

        if (entreno_id > 0) {

            var trn = db?.getDataWithId(DBHelper.TABLE_TRAININGS, entreno_id) as Training

            this.date = trn.date

            var pos_muscle: Int? = Utils.getPositionWithId(listMuscles!!, trn.muscle_group_id)

            spinnerGroups.setSelection(pos_muscle!!)

            Utils.runAfterOnTime(800) {
                val pos_exer: Int? =
                    Utils.getPositionWithId(listExercises!!, trn.exercise_id)
                spinnerExercises.setSelection(pos_exer!!)
            }

            editComment.setText(trn.comment)
            listSeries = Utils.seriesToarrayList(trn.series)!!
            listSeriesRecycler.adapter = SeriesAdapter(listSeries)
            txtNewEdit.text = getString(R.string.edit_training)
            textDate.isClickable = true
            textDate.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.item_popup_selector
                )
            )
        } else {
            txtNewEdit.text = getString(R.string.new_training)
        }
    }

    private fun clearData() {
        id = 0
        editComment.setText("")
        series = "";
        listSeries.clear()
        listSeriesRecycler.adapter!!.notifyDataSetChanged()

    }

    fun newMuscleGroup(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        val newMuscleDialog = NewMuscleGroupFragment.newInstance(0)
        newMuscleDialog.show(transaction, Utils.DIALOG_NEW_EDIT_MUSCLE_GROUP)
    }

    fun newExercise(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        val newExerciseDialog = muscle_id?.let { NewExerciseFragment.newInstance(0, it) }
        newExerciseDialog?.show(transaction, Utils.DIALOG_NEW_EDIT_EXERCISE)
    }

    fun saveEntreno(view: View) {

        var entreno = Training()
        entreno.date = date!!

        if (muscle_id!! > 0) {
            entreno.muscle_group_id = muscle_id!!
        } else {
            Toast.makeText(this, "Invalid Muscle", Toast.LENGTH_LONG).show()
            return
        }
        if (exercise_id!! > 0) {
            entreno.exercise_id = exercise_id!!
        } else {
            Toast.makeText(this, "Invalid Exercise", Toast.LENGTH_LONG).show()
            return
        }
        entreno.comment = editComment.text.toString()

        val seriesArr: ArrayList<Serie> = (listSeriesRecycler.adapter as SeriesAdapter).list!!

        if (seriesArr.size > 0) entreno.series = Utils.arrayListSeriesToJson(seriesArr)!! else {
            Toast.makeText(this, "Add serie", Toast.LENGTH_LONG).show()
            return
        }

        if (this.id!! > 0) {
            entreno.id = this.id!!
            val ret = db!!.updateDataWithId(entreno)
            if (ret) {
                Toast.makeText(this, "Ok entreno actualizado!!!", Toast.LENGTH_LONG).show()
            }
        } else {
            val ret = db!!.insertData(entreno)
            if (ret > 0) {
                Toast.makeText(this, "Ok entreno guardado!!!", Toast.LENGTH_LONG).show()
                clearData()
            }
        }

    }

    override fun setOnSaveData(table: String) {
        when (table) {
            DBHelper.TABLE_MUSCLE_GROUPS -> {
                val mscFragment: NewMuscleGroupFragment? =
                    supportFragmentManager.findFragmentByTag(Utils.DIALOG_NEW_EDIT_MUSCLE_GROUP) as NewMuscleGroupFragment
                mscFragment?.let {
                    it.dismiss()
                }

            }
            DBHelper.TABLE_EXERCISES -> {
                val exeFragment: NewExerciseFragment? =
                    supportFragmentManager.findFragmentByTag(Utils.DIALOG_NEW_EDIT_EXERCISE) as NewExerciseFragment
                exeFragment?.let {
                    it.dismiss()
                }
            }
        }
        setSpinnerData(table, muscle_id!!)
    }


    override fun setOnDateChange(date: String) {
        date.let {
            textDate.text = it
            this.date = it
        }
        val prev: DialogFragment? =
            supportFragmentManager.findFragmentByTag(Utils.DIALOG_DATE_PICKER) as DialogFragment
        prev?.let {
            it.dismiss()
        }
    }


}
