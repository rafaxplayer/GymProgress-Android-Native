package com.jrs.gymprogress.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.adapters.ExercisesListAdapter
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.MuscleGroup
import kotlinx.android.synthetic.main.fragment_exercises_list.*
import kotlinx.android.synthetic.main.fragment_exercises_list.view.*

class ExercisesListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var db: SqliteWrapper? = null
    var listExercises: ArrayList<Exercise>? = null
    var listMuscles:ArrayList<MuscleGroup>?=null
    var muscle_id = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SqliteWrapper(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        view.newExerciseBtn.setOnClickListener { newExercise() }
        exerciseList.layoutManager = LinearLayoutManager(activity)

        spinGroups.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                muscle_id = listMuscles!![position].id
                initRecycler(muscle_id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(context!!, "Nothing", Toast.LENGTH_SHORT).show()
            }
        }

        btnNewExercise.setOnClickListener{ newExercise() }
    }

    fun initSpinner(){
        listMuscles =
            db!!.getAllData(DBHelper.TABLE_MUSCLE_GROUPS) as ArrayList<MuscleGroup>
        listMuscles.let {

            val arrayMuscles = it?.map { it.name }
            val arrayAdapter =
                ArrayAdapter(context!!, R.layout.simple_spinner_item, arrayMuscles!!)
            spinGroups.adapter = arrayAdapter
        }

    }

    fun initRecycler(muscle_id: Int? = listMuscles?.get(0)?.id){
        listExercises = db!!.getDataWitColumnValue(DBHelper.TABLE_EXERCISES,DBHelper.MUSCLE_GROUP_ID,muscle_id.toString()) as ArrayList<Exercise>
        listExercises.let {
            exerciseList.adapter = ExercisesListAdapter(context!!, it!!)
        }

    }

    private fun newExercise() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val newExerciseDialog = NewExerciseFragment.newInstance(0,muscle_id)
        newExerciseDialog.show(transaction, Utils.DIALOG_NEW_EDIT_EXERCISE)
    }
}
