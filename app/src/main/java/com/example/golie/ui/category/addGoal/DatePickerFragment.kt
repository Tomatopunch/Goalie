package com.example.golie.ui.category.addGoal

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

import kotlinx.android.synthetic.main.add_goal_fragment.view.*
import java.util.*

class DatePickerFragment : DialogFragment, DatePickerDialog.OnDateSetListener {

    var theView: View

    constructor(view: View) : super(){
        this.theView = view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        val theMonth = month + 1
        val date = ("$year-$theMonth-$day")

        theView.addGoal_timeSpanDate.setText(date)
    }
}