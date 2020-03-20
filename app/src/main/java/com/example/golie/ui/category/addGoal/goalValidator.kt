package com.example.golie.ui.category.addGoal

import android.content.Context
import com.example.golie.R

fun validateInput(title: String, points: String, context: Context?): MutableList<String> {
    val invalidInput: MutableList<String> = ArrayList()

    if(title.isEmpty()) {
        invalidInput.add(context!!.getString(R.string.goalValidator_title))
    }
    else if(title.length > 30) {
        invalidInput.add(context!!.getString(R.string.goalValidator_titleMax))
    }

    //val numberRegex = "[0-9]".toRegex()
    if(points.isEmpty()) {
        invalidInput.add(context!!.getString(R.string.goalValidator_points))
    }

    return invalidInput
}
