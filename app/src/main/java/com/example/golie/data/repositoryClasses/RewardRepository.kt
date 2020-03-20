package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Reward
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class RewardRepository : dbCursorRepository(){


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun createReward(userId: String, newReward: Reward) : Task<DocumentReference> {

        return  db.collection("users/$userId/rewards").add(newReward)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createRewardWithSpecificId(userId: String, newReward: Reward, rewardId: String) : Task<Void> {

        //val newRewardMap = hashMapOf("id" to newReward.id)

        return  db.collection("users/$userId/rewards").document(rewardId).set(newReward)

    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllRewards (userId: String) : Task<QuerySnapshot> {

        return db.collection("users/$userId/rewards").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun updateReward(userId: String, rewardId: String, updatedReward: Reward) : Task<Void>{

        val updatedRewardMap = mapOf("title" to updatedReward.title, "price" to updatedReward.price)

        return db.collection("users/$userId/rewards").document(rewardId).update(updatedRewardMap)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun deleteReward(userId: String, rewardId: String) : Task<Void>{

        return db.collection("users/$userId/rewards").document(rewardId).delete()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}