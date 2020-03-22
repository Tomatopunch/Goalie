package com.example.golie.ui.category.addGoal

import android.content.Context
import com.example.golie.R

fun validateInput(title: String, points: String, context: Context?): MutableList<String> {
    val invalidInput: MutableList<String> = ArrayList()
    val longestTitle = 30

    if(title.isEmpty()) {
        invalidInput.add(context!!.getString(R.string.goalValidator_title))
    }
    else if(title.length > longestTitle) {
        invalidInput.add(context!!.getString(R.string.goalValidator_titleMax))
    }

    if(points.isEmpty()) {
        invalidInput.add(context!!.getString(R.string.goalValidator_points))
    }

    return invalidInput
}
