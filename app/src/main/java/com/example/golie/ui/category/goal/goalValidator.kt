package com.example.golie.ui.category.goal

import com.example.golie.R

fun validateInput(title: String, points: Int): MutableList<String> {
    val invalidInput: MutableList<String> = ArrayList()
    if(title.isEmpty()){
        invalidInput.add("Must enter a title.")
    }else if(title.length > 30){
        invalidInput.add("Title is too long. Max 30 characters")
    }


    return invalidInput
}
