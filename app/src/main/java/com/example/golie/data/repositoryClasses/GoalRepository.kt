package com.example.golie.data.repositoryClasses

import com.example.golie.data.dataClasses.Goal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class GoalRepository : DbCursorRepository() {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createGoal(userId : String, categoryId: String, newGoal: Goal) : Task<DocumentReference>{
        return db.collection("users/$userId/categories/$categoryId/allGoals").add(newGoal)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllGoalsWithinCategory(userId : String, categoryId: String) : Task<QuerySnapshot> {
        return db.collection("users/$userId/categories/$categoryId/allGoals").get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getGoalById(userId: String, categoryId: String, goalId: String) : Task<DocumentSnapshot> {
        return db.collection("users/$userId/categories/$categoryId/allGoals").document(goalId).get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun updateGoal(userId : String, categoryId: String, goalId: String, updatedGoal: Goal) : Task<Void> {
        val updatedGoalMap = mapOf("title" to updatedGoal.title, "timeSpan" to updatedGoal.timeSpan, "reoccurring" to updatedGoal.reoccurring, "points" to updatedGoal.points)
        return db.collection("users/$userId/categories/$categoryId/allGoals").document(goalId).update(updatedGoalMap)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateColorId(userId: String, categoryId: String, goalId: String, updatedColorId: Int) : Task<Void>{
        return db.collection("users/$userId/categories/$categoryId/allGoals").document(goalId).update("colorId", updatedColorId)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteGoal (userId : String, categoryId: String, goalId: String) : Task<Void> {
       return db.collection("users/$userId/categories/$categoryId/allGoals").document(goalId).delete()
    }
}
