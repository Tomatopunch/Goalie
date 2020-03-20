package com.example.golie.data.repositoryClasses

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity
import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentsToGoals
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.category_fragment.view.*


class CategoryRepository : dbCursorRepository() {


    private val goalRepository = GoalRepository()


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun createCategory(userId: String, newCategory: Category): Task<DocumentReference> {

        return db.collection("users/$userId/categories").add(newCategory)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getCategoryById(userId: String, categoryId: String) : Task<DocumentSnapshot> {
        return db.collection("users/$userId/categories").document(categoryId).get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllCategories(userId: String): Task<QuerySnapshot> {

        return db.collection("users/$userId/categories").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun deleteCategory(userId: String, categoryId: String, navController: NavController, view: View, context: Context) {

        // First; fetching and deleting (one at a time) all goals that belong to the category

        db.collection("users/$userId/categories/$categoryId/allGoals").get()

            .addOnSuccessListener { document -> //All goals were fetched successfully!
                val allGoals = documentsToGoals(document)
                if(allGoals.isEmpty()){
                    deleteEmptyCategory(userId, categoryId, view, navController, context)
                }
                var deleteCounter = 0
                for (document in allGoals) {
                    var idOfGoalToBeDeleted = document.id
                    goalRepository.deleteGoal(userId, categoryId, idOfGoalToBeDeleted)
                        .addOnSuccessListener {
                            deleteCounter += 1
                            if(deleteCounter == allGoals.size){
                                db.collection("users/$userId/categories/").document(categoryId)
                                    .delete()

                                    .addOnSuccessListener {
                                        view.category_progressBar.visibility = View.GONE
                                        navController.navigate(R.id.nav_home)
                                    }

                                    .addOnFailureListener {
                                        view.category_progressBar.visibility = View.GONE
                                        Toast.makeText(context, context.getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener{
                            view.category_progressBar.visibility = View.GONE
                            Toast.makeText(context, context.getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                        }
                }
            }

                // Second; all goals are (hopefully) deleted and it is (hopefully) safe to go on and delete the category (.......)

            .addOnFailureListener { exception -> // All goals were NOT fetched successfully :(
                Toast.makeText(context, context.getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteEmptyCategory(userId: String, categoryId: String, view: View, navController: NavController, context: Context){
        db.collection("users/$userId/categories/").document(categoryId)
            .delete()

            .addOnSuccessListener {
                view.category_progressBar.visibility = View.GONE
                navController.navigate(R.id.nav_home)
            }

            .addOnFailureListener {
                view.category_progressBar.visibility = View.GONE
                Toast.makeText(context, context.getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
            }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun setFavoriteCategoryId(userId: String, idOfNewFavorite: String): Task<Void> {

        val idOfFavMap = mapOf("favoriteCategoryId" to idOfNewFavorite)

        return db.collection("users").document(userId).update(idOfFavMap)

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////






}



