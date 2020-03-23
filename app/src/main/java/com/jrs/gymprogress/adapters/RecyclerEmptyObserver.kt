package com.jrs.gymprogress.adapters

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver


class RecyclerEmptyObserver(var adapter: Adapter<*>?, var emptyView: LinearLayout) :
    AdapterDataObserver() {
    init {
        checkData()
    }

    override fun onChanged() {
        super.onChanged()
        checkData()
    }

    fun checkData() {
        adapter?.let {

            if (it.itemCount > 0) {
                emptyView.visibility = View.GONE
            } else {
                emptyView.visibility = View.VISIBLE
            }
        }
    }


}