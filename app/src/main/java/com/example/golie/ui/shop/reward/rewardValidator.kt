package com.example.golie.ui.shop.reward


const val MAXLENGTHOFTITLE = 30

fun validateRewardInput(title: String, points: String ): String {

    var validationErrors = ""

    if(title.isEmpty()){
        validationErrors = "Must enter a title!"
    }

    else if(title.length > MAXLENGTHOFTITLE){
        validationErrors = "The title is too long! Please enter a title with max " + MAXLENGTHOFTITLE + " characters."
    }

    if(points.isEmpty()){
        validationErrors = "Must enter a point score!"
    }

    return validationErrors
}