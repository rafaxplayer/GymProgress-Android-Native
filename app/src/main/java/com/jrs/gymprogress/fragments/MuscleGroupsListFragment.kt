package com.jrs.gymprogress.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.adapters.MusclesListAdapter
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.MuscleGroup
import com.jrs.gymprogress.interfaces.SaveDataListener
import kotlinx.android.synthetic.main.activity_gym_progress.*
import kotlinx.android.synthetic.main.fragment_muscle_groups_list.*
import kotlinx.android.synthetic.main.fragment_muscle_groups_list.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [MuscleGroupsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MuscleGroupsListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var db: SqliteWrapper? = null
    var listMuscles: ArrayList<MuscleGroup>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SqliteWrapper(context!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_muscle_groups_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        view.newMuscleGroup.setOnClickListener { newMuscleGroup() }
    }

    fun initRecycler() {
        var listMuscles: ArrayList<MuscleGroup>? =
            db!!.getAllData(DBHelper.TABLE_MUSCLE_GROUPS) as ArrayList<MuscleGroup>
        musclesList.layoutManager = LinearLayoutManager(activity)
        musclesList.adapter = MusclesListAdapter(context!!, listMuscles!!, db!!)
    }

    private fun newMuscleGroup() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val newMuscleDialog = NewMuscleGroupFragment.newInstance(0)
        newMuscleDialog.show(transaction, Utils.DIALOG_NEW_EDIT_MUSCLE_GROUP)
    }


}
