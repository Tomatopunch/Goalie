package com.example.golie.data.repositoryClasses

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.documentsToGoals
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.category_fragment.view.*

class CategoryRepository : DbCursorRepository() {

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

        // fetching and deleting (one at a time) all goals that belong to the category

        db.collection("users/$userId/categories/$categoryId/allGoals").get()
            //All goals were fetched successfully!
            .addOnSuccessListener { document ->
                val allGoals = documentsToGoals(document)
                if(allGoals.isEmpty()){
                    deleteEmptyCategory(userId, categoryId, view, navController, context)
                }
                var deleteCounter = 0
                for (document in allGoals) {
                    val idOfGoalToBeDeleted = document.id
                    goalRepository.deleteGoal(userId, categoryId, idOfGoalToBeDeleted)
                        .addOnSuccessListener {
                            deleteCounter += 1
                            if(deleteCounter == allGoals.size){

                                // all goals are deleted and it is safe to go on and delete the category
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

            .addOnFailureListener {
                Toast.makeText(context, context.getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
            }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun deleteEmptyCategory(userId: String, categoryId: String, view: View, navController: NavController, context: Context){
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
}



