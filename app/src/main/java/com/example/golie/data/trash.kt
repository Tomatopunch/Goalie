package com.example.golie.data

import android.content.ContentValues
import android.util.Log
import com.example.golie.data.repositoryClasses.GoalRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot


/*


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun deleteAllGoalsWithinCategory (allGoals : QuerySnapshot, currentUserId: String) : Int {

val goalRepository = GoalRepository()

for (document in allGoals) {
var idOfGoalToBeDeleted = document.id
goalRepository.deleteGoal(currentUserId, currentCategoryId, idOfGoalToBeDeleted)

}

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


fun deleteAllGoalsWithinCategory (goalsToBeDeleted : QuerySnapshot, currentUserId: String, currentCategoryId: String) : Boolean {

var allGoalsProperlyDeleted = true

for (document in goalsToBeDeleted) {

var idOfCurrentGoal = document.id
goalRepository.deleteGoal(currentUserId, currentCategoryId, idOfCurrentGoal)
.addOnSuccessListener {  }
.addOnFailureListener { allGoalsProperlyDeleted = false}

}

return allGoalsProperlyDeleted

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun deleteCategory(currentUserId: String, currentCategoryId: String) : Task<Void> {

//First; fetching and deleting (one at the time) all goals that belong to the category
db.collection("users/" + currentUserId + "/categories/" + currentCategoryId + "/allGoals")
.get()
.addOnSuccessListener { result ->
for (document in result) {

var idOfGoalToBeDeleted = document.id
goalRepository.deleteGoal(currentUserId, currentCategoryId, idOfGoalToBeDeleted)
}

return db.collection("users/" + currentUserId + "/categories/").document(currentCategoryId).delete()


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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////



*/