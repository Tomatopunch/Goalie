package com.example.golie.data
import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.w3c.dom.Document
import java.util.*

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentsToCategories (documents : QuerySnapshot) : MutableList<Category> {

    var allCategories = mutableListOf<Category>()

    for (document in documents) {

        val category = document.toObject(Category::class.java)
        category.id = document.id
        allCategories.add(category)

        Log.d(ContentValues.TAG, "Success getting category with id ${document.id} and data ${document.data}")
        Log.d("the id of the category object", " " + category.id + "")
        Log.d("category object", " " + category + "")
    }

    return allCategories

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentsToGoals (documents : QuerySnapshot) : MutableList<Goal> {


    var allGoals = mutableListOf<Goal>()

    for (document in documents) {

        val goal = document.toObject(Goal::class.java)
        goal.id = document.id
        allGoals.add(goal)

    }

    return allGoals

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentsToRewards(documents : QuerySnapshot) : MutableList<Reward> {


    var allRewards = mutableListOf<Reward>()

    for (document in documents) {

        val reward = document.toObject(Reward::class.java)
        reward.id = document.id
        allRewards.add(reward)

    }

    return allRewards

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun doucumentToPoints (document: DocumentSnapshot) : Int {

    return document.data!!.getValue("points").toString().toInt()
}


