package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Reward
import com.google.firebase.firestore.FirebaseFirestore

class RewardRepository : dbCursorRepository(){


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createReward(currentUserId: String, newReward: Reward){

        val refToRewardsSubcollection = db.collection("users/" + currentUserId + "/rewards" )

        refToRewardsSubcollection.add(newReward)
            .addOnSuccessListener { documentReference ->

                Log.d(ContentValues.TAG, "Successfully added category with ID: " + documentReference.id + "within subcollection 'categories' for user " + currentUserId)

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding category", e)
                e.printStackTrace()

            }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAlLRewards (currentUserId: String) : MutableList<Reward>{

        var allRewards = mutableListOf<Reward>()

        db.collection("users/" + currentUserId + "/rewards")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val reward = document.toObject(Reward::class.java)
                    reward.id = document.id
                    allRewards.add(reward)

                    Log.d(ContentValues.TAG, "Success getting reward with id ${document.id} and data ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting rewards: ", exception)
            }

        return allRewards
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateReward(currentUserId: String, rewardId: String, updatedReward: Reward){


        val rewardRef = db.collection("users/" + currentUserId + "/rewards").document(rewardId)

        val updatedRewardMap = mapOf("title" to updatedReward.title, "price" to updatedReward.price)

        rewardRef
            .update(updatedRewardMap)
            .addOnSuccessListener { Log.d("Updating reward", "Reward with id " + rewardId + "successfully updated for user with id " + currentUserId) }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error updating reward", e) } //why?

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteReward(currentUserId: String, rewardId: String){

        db.collection("users/" + currentUserId + "/rewards").document(rewardId)
            .delete()
            .addOnSuccessListener { Log.d(ContentValues.TAG, "Reward successfully deleted!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting reward.", e) }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}