package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Goal
import com.google.firebase.firestore.FirebaseFirestore

class GoalRepository : dbCursorRepository() {



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createGoal(currentUserId : String, currentCategoryId: String, newGoal: Goal){

        val refToAllGoals = db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals")

        refToAllGoals.add(newGoal)
            .addOnSuccessListener { documentReference ->

                Log.d(ContentValues.TAG, "Successfully added category with ID: " + documentReference.id + "within subcollection 'categories' for user " + currentUserId)

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding category", e)
                e.printStackTrace()

            }

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllGoalsWithinCategory(currentUserId : String, currentCategoryId: String) : MutableList<Goal>{

        var allGoals = mutableListOf<Goal>()

        //Fetching all goals from database

        db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val goal = document.toObject(Goal::class.java)
                    goal.id = document.id
                    allGoals.add(goal)

                    Log.d(ContentValues.TAG, "Success getting goal with id ${document.id} and data ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting goals: ", exception)
            }

        return allGoals


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateGoal(currentUserId : String, currentCategoryId: String, goalId: String, updatedGoal: Goal){

        val categoryRef = db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/goalId").document(goalId)

        val updatedGoalMap = mapOf("title" to updatedGoal.title, "timeSpan" to updatedGoal.timeSpan, "reoccurring" to updatedGoal.reoccurring, "points" to updatedGoal.points)
        categoryRef
            .update(updatedGoalMap)
            .addOnSuccessListener { Log.d("Updating goal", "Goal with id " + goalId + " was updated successfully for user with id " + currentUserId) }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error updating goal", e) } //why?

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteGoal (currentUserId : String, currentCategoryId: String, goalId: String){

        db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals").document(goalId)
            .delete()
            .addOnSuccessListener { Log.d(ContentValues.TAG, "Goal successfully deleted!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting goal.", e) }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
