package com.example.golie.ui.home.addCategory

val MAXLENGTHOFTITLE = 30

fun validateCategoryInput(name: String): String {

    var validationErrors = ""

    if(name.isEmpty()){
        validationErrors = "Must enter a title!"
    }

    else if(name.length > MAXLENGTHOFTITLE){
        validationErrors = "The title is too long! Please enter a title with max " + MAXLENGTHOFTITLE + " characters."
    }


    return validationErrors
}