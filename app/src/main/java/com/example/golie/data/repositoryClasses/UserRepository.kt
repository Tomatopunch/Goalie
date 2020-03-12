package com.example.golie.data.repositoryClasses

import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class UserRepository : dbCursorRepository(){

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createUser(newUserId: String) : Task<Void> {

        val points = hashMapOf(
            "points" to 0
        )

        return  db.collection("users").document(newUserId).set(points)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createUserWithData(newUserId: String)  {

        val categoryRepository = CategoryRepository()
        val goalRepository = GoalRepository()
        val rewardRepository = RewardRepository()

        val points = hashMapOf("points" to 0)
        db.collection("users").document(newUserId).set(points)
            .addOnSuccessListener {

                categoryRepository.createCategory(newUserId, Category("Today"))
                    .addOnSuccessListener {document ->
                        goalRepository.createGoal(newUserId, document.id, Goal("Take a walk", "", false, 10))
                        goalRepository.createGoal(newUserId, document.id, Goal("Clean kitchen", "", false, 10))
                        goalRepository.createGoal(newUserId, document.id, Goal("Workout", "", false, 10))

                        categoryRepository.setFavoriteCategoryId(newUserId, document.id)
                    }

                categoryRepository.createCategory(newUserId, Category("This Week"))
                    .addOnSuccessListener {document->
                        goalRepository.createGoal(newUserId, document.id, Goal("Do homework", "", false, 30))
                    }

                rewardRepository.createReward(newUserId, Reward("Eat ice cram", 20))
                rewardRepository.createReward(newUserId, Reward("Watch a movie with friends", 30))
                rewardRepository.createReward(newUserId, Reward("Spa weekend with bestie", 200))


            }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUserById(userId: String): Task<DocumentSnapshot> {

        return  db.collection("users").document(userId).get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}