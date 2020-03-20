package com.jrs.gymprogress.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.jrs.gymprogress.R
import com.jrs.gymprogress.Utils
import com.jrs.gymprogress.interfaces.DatePickerChangeListener
import com.jrs.gymprogress.interfaces.SaveDataListener
import kotlinx.android.synthetic.main.fragment_date_picker.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DATE = "date"

/**
 * A simple [Fragment] subclass.
 * Use the [DatePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var paramDate: String? = null
    private var curDate: String? = ""
    private var dateListener: DatePickerChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramDate = it.getString(ARG_DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is SaveDataListener) {
            this.dateListener = activity as DatePickerChangeListener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarDatePicker.setOnDateChangeListener { view, year, month, dayOfMonth ->
            curDate = Utils.formatPartialsDate(year, month + 1, dayOfMonth)
            curDate?.let { dateListener?.setOnDateChange(it) }

        }

    }

    companion object {

        fun newInstance(date: String) =
            DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATE, date)

                }
            }
    }
}
