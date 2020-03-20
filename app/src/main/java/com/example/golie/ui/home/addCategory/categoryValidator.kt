package com.example.golie.ui.home.addCategory

import android.content.Context
import com.example.golie.R

const val MAXLENGTHOFTITLE = 30

fun validateCategoryInput(name: String, context: Context?): String {

    var validationErrors = ""

    if(name.isEmpty()){
        validationErrors = context!!.getString(R.string.categoryValidator_title)
    }

    else if(name.length > MAXLENGTHOFTITLE){
        validationErrors = context!!.getString(R.string.categoryValidator_titleMax) + MAXLENGTHOFTITLE + context.getString(
                    R.string.categoryValidator_characters)
    }


    return validationErrors
}