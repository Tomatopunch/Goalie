package com.example.golie.ui.category.goal

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.example.golie.R
import kotlinx.android.synthetic.main.add_goal_fragment.*
import kotlinx.android.synthetic.main.add_goal_fragment.view.*
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(context!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {


        val theMonth = month + 1
        val date = ("$year-$theMonth-$day")

        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.add_goal_fragment, null)
        view.addGoal_timeSpanDate.setText(date, TextView.BufferType.EDITABLE)

        val timeSpanText = view.addGoal_timeSpanDate.editableText.toString()

        Log.d("checkSpan", "$timeSpanText")

    }
}