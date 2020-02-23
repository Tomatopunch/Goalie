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

    fun createReward(currentUserId: String, newReward: Reward) : Task<DocumentReference> {

        return  db.collection("users/" + currentUserId + "/rewards" ).add(newReward)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAlLRewards (currentUserId: String) : Task<QuerySnapshot> {

        return db.collection("users/" + currentUserId + "/rewards").get()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun updateReward(currentUserId: String, rewardId: String, updatedReward: Reward) : Task<Void>{

        val updatedRewardMap = mapOf("title" to updatedReward.title, "price" to updatedReward.price)

        return db.collection("users/" + currentUserId + "/rewards").document(rewardId).update(updatedRewardMap)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteReward(currentUserId: String, rewardId: String) : Task<Void>{

        return db.collection("users/" + currentUserId + "/rewards").document(rewardId).delete()

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}