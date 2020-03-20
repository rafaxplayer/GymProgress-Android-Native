package com.jrs.gymprogress.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.interfaces.SaveDataListener
import kotlinx.android.synthetic.main.fragment_new_muscle_group.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MUSCLE_ID = "muscle_id"

/**
 * A simple [Fragment] subclass.
 * Use the [NewMuscleGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewMuscleGroupFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    var db: SqliteWrapper? = null
    var muscleid: Int = 0
    private var listener: SaveDataListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            muscleid = it.getInt(MUSCLE_ID)
        }
        db = SqliteWrapper(context!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_muscle_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSaveMuscle.setOnClickListener { saveMuscleGroup() }
        if ( muscleid > 0 ) {
            txtNewEditMuscle.text = getString(R.string.edit_group)
            var muscle = db!!.getDataWithId(DBHelper.TABLE_MUSCLE_GROUPS,muscleid) as MuscleGroup
            editTextMuscle.setText( muscle.name)
        } else { txtNewEditMuscle.text = getString(R.string.new_group) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is SaveDataListener) {
            this.listener = activity as SaveDataListener
        }
    }

    fun clearData() {
        muscleid = 0
        txtNewEditMuscle.text = "";
    }

    fun saveMuscleGroup() {
        if (editTextMuscle.text.isEmpty()) {
            Toast.makeText(activity, "Tu nombe de grupo muscular esta vacio", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (db!!.checkIfDataExists(
                DBHelper.TABLE_MUSCLE_GROUPS,
                DBHelper.NAME,
                editTextMuscle.text.toString()
            )
        ) {
            Toast.makeText(activity, "Tu Nombe de Grupo muscular ya existe", Toast.LENGTH_LONG)
                .show()
            return
        }

        val muscleName = editTextMuscle.text.toString()
        val muscle = MuscleGroup()
        muscle.name = muscleName
        muscle.img_path = ""

        if (muscleid <= 0) {
            val ret = db!!.insertData(muscle)
            if (ret > 0) {
                Toast.makeText(
                    activity,
                    "Grupo muscular $muscleName guardado!!!",
                    Toast.LENGTH_LONG
                ).show()
                clearData()
                listener?.setOnSaveData(DBHelper.TABLE_MUSCLE_GROUPS)
            } else {
                Toast.makeText(
                    activity,
                    "Ocurrio un error al guardar $muscleName",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            muscle.id = muscleid
            val ret = db?.updateDataWithId(muscle)
            if (ret!!) {
                Toast.makeText(
                    activity,
                    "Grupo muscular $muscleName actualizado!!!",
                    Toast.LENGTH_LONG
                ).show()
                clearData()
                listener?.setOnSaveData(DBHelper.TABLE_MUSCLE_GROUPS)
            } else {
                Toast.makeText(
                    activity,
                    "Ocurrio un error al actualizar $muscleName",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    companion object {

        fun newInstance(muscle_id: Int) = NewMuscleGroupFragment().apply {
            arguments = Bundle().apply {
                putInt(MUSCLE_ID, muscle_id)

            }
        }

    }
}
