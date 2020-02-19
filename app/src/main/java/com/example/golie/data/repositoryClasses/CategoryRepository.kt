package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.google.firebase.firestore.FirebaseFirestore


class CategoryRepository : dbCursorRepository()
{


    private val goalRepository = GoalRepository()


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createCategory(currentUserId: String , newCategory : Category)  {

        val refToCategoriesSubcollection = db.collection("users/" + currentUserId + "/categories")

        refToCategoriesSubcollection.add(newCategory)
            .addOnSuccessListener { documentReference ->

                Log.d(ContentValues.TAG, "Successfully added category with ID: " + documentReference.id + "within subcollection 'categories' for user " + currentUserId)

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding category", e)
                e.printStackTrace()

            }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllCategories(currentUserId : String) : MutableList<Category>{

        var allCategories = mutableListOf<Category>()


        db.collection("users/" + currentUserId + "/categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val category = document.toObject(Category::class.java)
                    category.id = document.id
                    allCategories.add(category)

                    Log.d(ContentValues.TAG, "Success getting category with id ${document.id} and data ${document.data}")
                    Log.d("the id of the category object", " " + category.id + "")
                    Log.d("category object", " " + category + "")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting categories: ", exception)
            }

        return allCategories


    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateCategory(currentUserId: String, categoryId: String, updatedCategory: Category) {

        val categoryRef = db.collection("users/" + currentUserId + "/categories").document(categoryId)

        val updatedCategoryMap = mapOf("name" to updatedCategory.name) //This might seem uneccessary but is included to make all update functions alike and to make this update function more extendable if another attribute was to be added in the Category class

        categoryRef
            .update(updatedCategoryMap) //Why is this an unresolved reference?
            .addOnSuccessListener { Log.d("Updating category name", "Catgory name successfully updated for category with id " + categoryId + " for user with id " + currentUserId) }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating category name", e) } //why?
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