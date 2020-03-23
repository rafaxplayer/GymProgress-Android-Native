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
import com.jrs.gymprogress.database.SqliteWrapper
import com.jrs.gymprogress.database.models.Training
import kotlinx.android.synthetic.main.activity_gym_progress.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class GymProgressActivity : AppCompatActivity() {

    var db: SqliteWrapper? = null
    var curDate: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_gym_progress)
        setSupportActionBar(toolbar)

        db = SqliteWrapper(this)
        curDate = Utils.convertTimeToISODate(calendarEntrenos.date)
        listEntrenosRecycler.layoutManager = LinearLayoutManager(this)
        calendarEntrenos.setOnDateChangeListener { view, year, month, dayOfMonth ->
            curDate = Utils.formatPartialsDate(year, month+1, dayOfMonth)
            txtEntrenoDate.text = "Entreno dia ${ curDate }"
            setEntrenos(curDate!!)
        }

    }

    override fun onResume() {
        super.onResume()
        setEntrenos(curDate!!)
        txtEntrenoDate.text = "Entreno dia ${ Utils.reformatDate(curDate!!) }"
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

    fun today(view:View){
        calendarEntrenos.date= Calendar.getInstance().timeInMillis
    }

    fun newEntreno(view: View) {
        val intent = Intent(this, TrainingsActivity::class.java)
        intent.apply {
            putExtra("date", curDate!!)
            putExtra("entreno_id", 0)
            startActivity(this)
        }

    }

    private fun setEntrenos(date: String): ArrayList<Training> {

        var listEntrenos: ArrayList<Training> = db!!.getTrainingsWithDate(date)
        listEntrenosRecycler.adapter = TrainingsAdapter(this, listEntrenos,db!!)
        listEntrenosRecycler.adapter?.let {
            it.registerAdapterDataObserver(RecyclerEmptyObserver(it, emptyList))
        }
        return listEntrenos
    }

}

