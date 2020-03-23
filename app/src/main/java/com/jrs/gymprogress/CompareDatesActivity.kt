package com.jrs.gymprogress

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrs.gymprogress.adapters.RecyclerEmptyObserver
import com.jrs.gymprogress.adapters.TrainingsAdapter
import com.jrs.gymprogress.database.DBHelper
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Serie
import com.jrs.gymprogress.database.models.Training
import kotlinx.android.synthetic.main.activity_compare_dates.*
import kotlinx.android.synthetic.main.list_entrenos_card.*
import kotlinx.android.synthetic.main.toolbar.*

class CompareDatesActivity : AppCompatActivity() {

    var training_id: Int = 0
    var exercise_id: Int = 0
    var db: SqliteWrapper? = null
    var training_card: Training? = null
    var listTraining: ArrayList<Training>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_dates)
        setSupportActionBar(toolbar)
        db = SqliteWrapper(this)
        cardButtons.visibility = View.GONE
        listTrainingCompare.layoutManager = LinearLayoutManager(this)
        intent.extras?.let { bun ->
            training_id = bun.getInt("training_id")
            exercise_id = bun.getInt("exercise_id")

            training_card = db?.getDataWithId(DBHelper.TABLE_TRAININGS, training_id) as Training
            training_card.let {
                textEntrenoName.text = it?.exercise_name
                textDate.text = Utils.reformatDate(it?.date!!)
                textComment.text = it?.comment
                val listSeries: ArrayList<Serie>? = Utils.seriesToarrayList(it?.series!!)
                Utils.setSeriesToTableView(this, tableSeries, listSeries!!)
            }
            listTraining = db?.getDataWitColumnValue(
                DBHelper.TABLE_TRAININGS,
                DBHelper.EXERCISE_ID,
                exercise_id.toString()
            ) as ArrayList<Training>

            listTraining.let { list ->
                var filteringList = list?.filterNot { tr ->

                    tr.id == this.training_card?.id
                }

                listTrainingCompare.adapter =
                    TrainingsAdapter(this, filteringList as ArrayList<Training>, db!!)
                listTrainingCompare.adapter?.let {
                    it.registerAdapterDataObserver(RecyclerEmptyObserver(it, emptyList))
                }
            }


        }


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
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}
