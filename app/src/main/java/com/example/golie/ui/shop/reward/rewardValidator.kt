package com.example.golie.ui.shop.reward

import android.content.Context
import com.example.golie.R


const val MAXLENGTHOFTITLE = 30

fun validateRewardInput(title: String, points: String, context: Context?): String {

    var validationErrors = ""

    if(title.isEmpty()){
        validationErrors = context!!.getString(R.string.rewardValidator_title)
    }

    else if(title.length > MAXLENGTHOFTITLE){
        validationErrors = context!!.getString(R.string.rewardValidator_titleMax) + MAXLENGTHOFTITLE + context.getString(
                    R.string.rewardValidator_characters)
    }

    if(points.isEmpty()){
        validationErrors = context!!.getString(R.string.rewardValidator_points)
    }

    return validationErrors
}