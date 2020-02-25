package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Goal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GoalRepository : dbCursorRepository() {



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Funkar
    fun createGoal(currentUserId : String, currentCategoryId: String, newGoal: Goal) : Task<DocumentReference>{

        return db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals").add(newGoal)
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Funkar
    fun getAllGoalsWithinCategory(currentUserId : String, currentCategoryId: String) : Task<QuerySnapshot> {

        return db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Funkar
    fun updateGoal(currentUserId : String, currentCategoryId: String, goalId: String, updatedGoal: Goal) : Task<Void> {

        val updatedGoalMap = mapOf("title" to updatedGoal.title, "timeSpan" to updatedGoal.timeSpan, "reoccurring" to updatedGoal.reoccurring, "points" to updatedGoal.points)
        return db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals").document(goalId).update(updatedGoalMap)

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Funkar
    fun deleteGoal (currentUserId : String, currentCategoryId: String, goalId: String) : Task<Void> {

       return db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals").document(goalId).delete()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
