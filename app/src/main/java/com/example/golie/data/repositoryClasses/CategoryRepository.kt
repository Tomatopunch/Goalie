package com.example.golie.data.repositoryClasses

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity
import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*


class CategoryRepository : dbCursorRepository() {


    private val goalRepository = GoalRepository()


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Funkar
    fun createCategory(currentUserId: String, newCategory: Category): Task<DocumentReference> {

        return db.collection("users/$currentUserId/categories").add(newCategory)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getCategoryById(currentUserId: String, categoryId: String) : Task<DocumentSnapshot> {
        return db.collection("users/$currentUserId/categories").document(categoryId).get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Funkar
    fun getAllCategories(currentUserId: String): Task<QuerySnapshot> {

        return db.collection("users/$currentUserId/categories").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Funkar
    fun updateCategory(currentUserId: String, categoryId: String, updatedCategory: Category): Task<Void> {

        val updatedCategoryMap = mapOf("name" to updatedCategory.name) //This might seem unnecessary but is included to make all update functions alike and to make this update function more extendable if another attribute was to be added in the Category class

        return db.collection("users/$currentUserId/categories").document(categoryId).update(updatedCategoryMap)

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Funkar!!
    fun deleteCategory(currentUserId: String, currentCategoryId: String, mainActivity: MainActivity) {

        // First; fetching and deleting (one at a time) all goals that belong to the category

        db.collection("users/$currentUserId/categories/$currentCategoryId/allGoals").get()

            .addOnSuccessListener { allGoals -> //All goals were fetched successfully!

                for (document in allGoals) {
                    var idOfGoalToBeDeleted = document.id
                    goalRepository.deleteGoal(currentUserId, currentCategoryId, idOfGoalToBeDeleted)
                }

                // Second; all goals are (hopefully) deleted and it is (hopefully) safe to go on and delete the category

               db.collection("users/$currentUserId/categories/").document(currentCategoryId)
                    .delete()

                   .addOnSuccessListener {mainActivity.recreate()}
                   .addOnFailureListener {}


            }

            .addOnFailureListener { exception -> // All goals were NOT fetched successfully :(
                Log.d(ContentValues.TAG, "Error getting goals: ", exception)
            }


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun setFavoriteCategoryId(currentUserId: String, idOfNewFavorite: String): Task<Void> {

        val idOfFavMap = hashMapOf("idOfFavoriteCategory" to idOfNewFavorite)

        return db.collection("users/$currentUserId/favorites").document("favoriteCategory").set(idOfFavMap)

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun getFavoriteCategoryId(currentUserId: String): Task<DocumentSnapshot> {

        return db.collection("users/$currentUserId/favorites").document("favoriteCategory").get()

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}



