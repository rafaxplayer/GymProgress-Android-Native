package com.jrs.gymprogress.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.adapters.TrainingsAdapter
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Exercise
import com.jrs.gymprogress.database.models.Training
import kotlinx.android.synthetic.main.activity_gym_progress.*
import kotlinx.android.synthetic.main.fragment_compare_trainings.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TRAINING_ID = "training_id"
private const val ARG_EXERCISE_ID = "exercise_id"

class CompareTrainingsFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var trainingId: Int? = null
    private  var exerciseId:Int? = null
    private var db:SqliteWrapper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trainingId = it.getInt(ARG_TRAINING_ID)
            exerciseId = it.getInt(ARG_EXERCISE_ID)
        }
        db = SqliteWrapper(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compare_trainings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listCompareRecycler.layoutManager = LinearLayoutManager(context)
        setCardData(trainingId!!)
        initListRecycler(exerciseId!!)
    }

    fun setCardData(trainingId: Int){
        val training:Training?= db!!.getDataWithId(DBHelper.TABLE_TRAININGS, trainingId) as Training
        training?.let {

            textEntrenoName.text = it.exercise_name
            textComment2.text = it.comment
            textDate.text= Utils.reformatDate(it.date)

        }
    }

    fun initListRecycler(exercise_id:Int){
        val listTrainings:ArrayList<Training>? = db!!.getDataWitColumnValue(DBHelper.TABLE_TRAININGS,DBHelper.EXERCISE_ID,exercise_id.toString()) as ArrayList<Training>
        listTrainings?.let {
            listCompareRecycler.adapter = TrainingsAdapter(context!!, it,db!!)
        }
    }

    companion object {

        fun newInstance(trainingId: Int, exercise_id:Int) =
            CompareTrainingsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TRAINING_ID,trainingId)
                    putInt(ARG_EXERCISE_ID,exercise_id)
                }
            }
    }
}
