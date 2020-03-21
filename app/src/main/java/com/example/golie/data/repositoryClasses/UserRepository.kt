package com.example.golie.data.repositoryClasses

import com.example.golie.MainActivity
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class UserRepository : DbCursorRepository(){

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createUserWithData(newUserId: String, mainActivity: MainActivity) {

        val categoryRepository = CategoryRepository()
        val goalRepository = GoalRepository()
        val rewardRepository = RewardRepository()

        val initialDataMap = hashMapOf("points" to 0, "favoriteCategoryId" to "")
        db.collection("users").document(newUserId).set(initialDataMap)

            .addOnSuccessListener {
                categoryRepository.createCategory(newUserId, Category("Today"))
                    .addOnSuccessListener { document ->
                        categoryRepository.setFavoriteCategoryId(newUserId, document.id)
                            .addOnSuccessListener {
                                goalRepository.createGoal(
                                    newUserId,
                                    document.id,
                                    Goal("Take a walk", "", false, 10)
                                )
                                    .addOnSuccessListener {
                                        goalRepository.createGoal(
                                            newUserId,
                                            document.id,
                                            Goal("Clean kitchen", "", false, 10)
                                        )
                                            .addOnSuccessListener {
                                                goalRepository.createGoal(
                                                    newUserId,
                                                    document.id,
                                                    Goal("Workout", "", false, 10)
                                                )
                                                    .addOnSuccessListener {
                                                        categoryRepository.createCategory(
                                                            newUserId,
                                                            Category("This Week")
                                                        )
                                                            .addOnSuccessListener {document->
                                                                goalRepository.createGoal(
                                                                    newUserId,
                                                                    document.id,
                                                                    Goal(
                                                                        "Do homework",
                                                                        "",
                                                                        false,
                                                                        30
                                                                    )
                                                                )
                                                                    .addOnSuccessListener {
                                                                        rewardRepository.createReward(
                                                                            newUserId,
                                                                            Reward(
                                                                                "Eat ice cram",
                                                                                20
                                                                            )
                                                                        )
                                                                            .addOnSuccessListener {
                                                                                rewardRepository.createReward(
                                                                                    newUserId,
                                                                                    Reward(
                                                                                        "Watch a movie with friends",
                                                                                        30
                                                                                    )
                                                                                )
                                                                                    .addOnSuccessListener {
                                                                                        rewardRepository.createReward(
                                                                                            newUserId,
                                                                                            Reward(
                                                                                                "Spa weekend with bestie",
                                                                                                200
                                                                                            )
                                                                                        )
                                                                                            .addOnSuccessListener {
                                                                                                mainActivity.recreate()
                                                                                            }

                                                                                    }
                                                                            }
                                                                    }
                                                            }
                                                    }
                                            }
                                    }
                            }
                    }
            }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUserById(userId: String): Task<DocumentSnapshot> {
        return  db.collection("users").document(userId).get()
    }
}