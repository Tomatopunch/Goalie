package com.example.golie.data.repositoryClasses

import com.example.golie.data.dataClasses.Reward
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot

class RewardRepository : DbCursorRepository(){

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    fun createReward(userId: String, newReward: Reward) : Task<DocumentReference> {
        return  db.collection("users/$userId/rewards").add(newReward)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createRewardWithSpecificId(userId: String, newReward: Reward, rewardId: String) : Task<Void> {
        return  db.collection("users/$userId/rewards").document(rewardId).set(newReward)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllRewards (userId: String) : Task<QuerySnapshot> {
        return db.collection("users/$userId/rewards").get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteReward(userId: String, rewardId: String) : Task<Void>{
        return db.collection("users/$userId/rewards").document(rewardId).delete()
    }
}