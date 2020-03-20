package com.jrs.gymprogress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.fragments.ExercisesListFragment
import com.jrs.gymprogress.fragments.MuscleGroupsListFragment
import com.jrs.gymprogress.interfaces.SaveDataListener
import kotlinx.android.synthetic.main.toolbar.*

class SettingsActivity : AppCompatActivity() ,SaveDataListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
    }

    override fun setOnSaveData(table: String) {
        when(table){
            DBHelper.TABLE_MUSCLE_GROUPS->{
                val mscFragment: MuscleGroupsListFragment? = supportFragmentManager.findFragmentById(R.id.fragmentListMuscles) as MuscleGroupsListFragment
                mscFragment?.initRecycler()

            }
            DBHelper.TABLE_EXERCISES->{
                val exeFragment:ExercisesListFragment? = supportFragmentManager.findFragmentById(R.id.fragmentListExercises) as ExercisesListFragment
                exeFragment?.initRecycler()
            }
        }
    }

}
