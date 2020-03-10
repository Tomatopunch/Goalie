package com.example.golie.data
import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward
import com.example.golie.data.repositoryClasses.GoalRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.w3c.dom.Document
import java.util.*

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentToCategory (document : DocumentSnapshot) : Category {

    val category = document.toObject(Category::class.java)
    category!!.id = document.id
    return category
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentToGoal (document : DocumentSnapshot) : Goal {

    val goal = document.toObject(Goal::class.java)
    goal!!.id = document.id
    return goal
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentToReward(document : DocumentSnapshot) : Reward {

    val reward = document.toObject(Reward::class.java)
    reward!!.id = document.id
    return reward
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentsToCategories (documents : QuerySnapshot) : MutableList<Category> {

    var allCategories = mutableListOf<Category>()

    for (document in documents) {

        val category = documentToCategory(document)
        allCategories.add(category)

    }

    return allCategories

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentsToGoals (documents : QuerySnapshot) : MutableList<Goal> {


    var allGoals = mutableListOf<Goal>()

    for (document in documents) {

        val goal = documentToGoal(document)
        allGoals.add(goal)

    }

    return allGoals

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentsToRewards(documents : QuerySnapshot) : MutableList<Reward> {


    var allRewards = mutableListOf<Reward>()

    for (document in documents) {

        val reward = documentToReward(document)
        allRewards.add(reward)

    }

    return allRewards

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentToPoints (document: DocumentSnapshot) : Int {

    return document.data!!.getValue("points").toString().toInt()
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun documentToFavoriteCateoryId (document: DocumentSnapshot) : String {

    return document.data!!.getValue("idOfFavoriteCategory").toString()
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


