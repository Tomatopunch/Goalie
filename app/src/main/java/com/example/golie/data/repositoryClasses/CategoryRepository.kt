package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class CategoryRepository : dbCursorRepository()
{


    private val goalRepository = GoalRepository()


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createCategory(currentUserId: String , newCategory : Category) : Task<DocumentReference> {

        return  db.collection("users/" + currentUserId + "/categories").add(newCategory)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllCategories(currentUserId : String) : Task<QuerySnapshot> {

        return db.collection("users/" + currentUserId + "/categories").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateCategory(currentUserId: String, categoryId: String, updatedCategory: Category) : Task<Void> {

        val updatedCategoryMap = mapOf("name" to updatedCategory.name) //This might seem uneccessary but is included to make all update functions alike and to make this update function more extendable if another attribute was to be added in the Category class

        return db.collection("users/" + currentUserId + "/categories").document(categoryId).update(updatedCategoryMap)

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteCategory(currentUserId: String, currentCategoryId: String) {

        //First; fetching and deleting (one at the time) all goals that belong to the category
        db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var idOfGoalToBeDeleted = document.id
                    goalRepository.deleteGoal(currentUserId, currentCategoryId, idOfGoalToBeDeleted)
                    Log.d(ContentValues.TAG, "Success getting goal with id ${document.id} and data ${document.data}")

                }
            }

            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting goal: ", exception)
            }

        //Next; deleting the category itself
        db.collection("users/" + currentUserId + "/categories/").document(currentCategoryId)
            .delete()
            .addOnSuccessListener { Log.d(ContentValues.TAG, "Category successfully deleted!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting category.", e) }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}