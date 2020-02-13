package com.example.golie.ui.category.goal

fun validateInput(title: String, points: String): MutableList<String> {
    val invalidInput: MutableList<String> = ArrayList()
    if(title.isEmpty()){
        invalidInput.add("Title: Must enter a title.")
    }else if(title.length > 30){
        invalidInput.add("Title: It's too long. Max 30 characters")
    }

    val numberRegex = "[0-9]".toRegex()
    if(points.isEmpty()){
        invalidInput.add("Points: You need to add some points")
    }

    return invalidInput
}
