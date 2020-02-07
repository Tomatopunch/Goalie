package com.example.golie.ui.category.goal

fun validateInput(title: String, points: String): MutableList<String> {
    val invalidInput: MutableList<String> = ArrayList()
    if(title.isEmpty()){
        invalidInput.add("Must enter a title.")
    }else if(title.length > 30){
        invalidInput.add("Title is too long. Max 30 characters")
    }

    val numberRegex = "[0-9]".toRegex()
    if(!points.matches(numberRegex)){
        invalidInput.add("Only numbers allowed.")
    }else if(points == "0"){
        invalidInput.add("You need to add some points")
    }

    return invalidInput
}
