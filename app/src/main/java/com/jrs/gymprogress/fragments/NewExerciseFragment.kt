package com.jrs.gymprogress.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.interfaces.SaveDataListener
import kotlinx.android.synthetic.main.fragment_new_exercise.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MUSCLE_ID = "muscle_id"
private const val EXERCISE_ID = "exercise_id"

/**
 * A simple [Fragment] subclass.
 * Use the [NewExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewExerciseFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var exerciseId: Int = 0
    private var muscleid: Int = 0
    private var db: SqliteWrapper? = null
    private var listMuscles: ArrayList<MuscleGroup>? = null
    private var listener: SaveDataListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exerciseId = it.getInt(EXERCISE_ID)
            muscleid = it.getInt(MUSCLE_ID)
            println(it)
        }
        db = SqliteWrapper(context!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is SaveDataListener) {
            this.listener = activity as SaveDataListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_exercise, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listMuscles = db!!.getAllData(DBHelper.TABLE_MUSCLE_GROUPS) as ArrayList<MuscleGroup>
        val arrayMuscles = listMuscles!!.map { it.name }
        val arrayAdapter =
            ArrayAdapter(context!!, R.layout.simple_spinner_item, arrayMuscles)

        spinnerMuscles.adapter = arrayAdapter

        if (muscleid > 0) {
            var muscle_pos = Utils.getPositionWithId(listMuscles!!, muscleid)
            spinnerMuscles.setSelection(muscle_pos)

        }

        spinnerMuscles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                muscleid = listMuscles!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(activity, "Nothing", Toast.LENGTH_SHORT).show()
            }
        }

        btnSaveExercise.setOnClickListener { saveExercise() }

        if (exerciseId > 0) {
            txtNewEditExercise.text = getString(R.string.edit_exercise)
            val exercise = db!!.getDataWithId(DBHelper.TABLE_EXERCISES, exerciseId) as Exercise
            edittextEjercicio.setText(exercise.name)
        } else {
            txtNewEditExercise.text = getString(R.string.new_exercise)
        }
    }


    fun clearData() {
        edittextEjercicio.setText("")
        exerciseId = 0
        muscleid = 0
    }

    private fun saveExercise() {
        if (edittextEjercicio.text.isEmpty()) {
            Toast.makeText(activity, "Tu Nombe de ejercicio etavacio", Toast.LENGTH_LONG).show()
            return
        }

        if (db!!.checkIfDataExists(
                DBHelper.TABLE_EXERCISES,
                DBHelper.NAME,
                edittextEjercicio.text.toString()
            )
        ) {
            Toast.makeText(activity, "Tu Nombe de ejercicio ya existe", Toast.LENGTH_LONG).show()
            return
        }

        if (muscleid <= 0) {
            Toast.makeText(activity, "Selecciona un grupo muscular", Toast.LENGTH_LONG).show()
            return
        }

        val exerciseName = edittextEjercicio.text.toString()
        val exer = Exercise()
        exer.muscle_group_id = muscleid
        exer.name = exerciseName

        if (exerciseId <= 0) {
            val ret = db!!.insertData(exer)
            if (ret > 0) {
                Toast.makeText(activity, "Ejercicio $exerciseName guardado!!!", Toast.LENGTH_LONG)
                    .show()
                clearData()
                listener?.setOnSaveData(DBHelper.TABLE_EXERCISES)
            } else {
                Toast.makeText(
                    activity,
                    "Ocurrio un error al guardar $exerciseName",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            exer.id = exerciseId
            val ret = db?.updateDataWithId(exer)
            if (ret!!) {
                Toast.makeText(
                    activity,
                    "Ejecicio $exerciseName actualizado con exito!!!",
                    Toast.LENGTH_LONG
                ).show()
                listener?.setOnSaveData(DBHelper.TABLE_EXERCISES)
            } else {
                Toast.makeText(
                    activity,
                    "Ocurrio un error al actualizar $exerciseName",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }


    companion object {

        fun newInstance(exercise_id: Int, muscle_id: Int) =
            NewExerciseFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXERCISE_ID, exercise_id)
                    putInt(MUSCLE_ID, muscle_id)

                }
            }
    }


}
