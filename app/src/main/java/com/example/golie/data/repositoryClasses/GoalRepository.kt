package com.example.golie.data.repositoryClasses

import com.example.golie.data.dataClasses.Goal
import com.example.golie.ui.category.CategoryFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class GoalRepository : dbCursorRepository() {



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun createGoal(currentUserId : String, currentCategoryId: String, newGoal: Goal) : Task<DocumentReference>{

        return db.collection("users/$currentUserId/categories/$currentCategoryId/allGoals").add(newGoal)
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun getAllGoalsWithinCategory(currentUserId : String, currentCategoryId: String) : Task<QuerySnapshot> {

        return db.collection("users/$currentUserId/categories/$currentCategoryId/allGoals").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getGoalById(currentUserId: String, categoryId: String, goalId: String) : Task<DocumentSnapshot> {
        return db.collection("users/$currentUserId/categories/$categoryId/allGoals").document(goalId).get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun updateGoal(currentUserId : String, currentCategoryId: String, goalId: String, updatedGoal: Goal) : Task<Void> {

        val updatedGoalMap = mapOf("title" to updatedGoal.title, "timeSpan" to updatedGoal.timeSpan, "reoccurring" to updatedGoal.reoccurring, "points" to updatedGoal.points)
        return db.collection("users/$currentUserId/categories/$currentCategoryId/allGoals").document(goalId).update(updatedGoalMap)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateColorId(currentUserId: String, currentCategoryId: String, goalId: String, updatedColorId: Int) : Task<Void>{

        return db.collection("users/$currentUserId/categories/$currentCategoryId/allGoals").document(goalId).update("colorId", updatedColorId)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun deleteGoal (currentUserId : String, currentCategoryId: String, goalId: String) : Task<Void> {

       return db.collection("users/$currentUserId/categories/$currentCategoryId/allGoals").document(goalId).delete()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun verifyGoalStatus(allGoals: MutableList<Goal>, categoryFragment: CategoryFragment){

        for((index, goal) in allGoals.withIndex()){
            if(goal.colorId == -1){ // colorId can be id for red or green, or -1 for a non-existent color.
                continue
            }
            else{
                categoryFragment.setBackgroundColor(index, goal.colorId, true) // index is one too high
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
