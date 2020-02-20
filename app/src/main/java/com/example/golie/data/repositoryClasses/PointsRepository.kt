package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class PointsRepository : dbCursorRepository(){

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   fun getPoints(currentUserId: String) : Task<DocumentSnapshot> {

       return db.collection("users/" + currentUserId + "/points").document("currentPoints").get()

   }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun setPoints(currentUserId: String, points: Int){

        val pointsDocumentData = hashMapOf(
            "points" to points
        )


        db.collection("users/" + currentUserId + "/points").document("currentPoints")
            .set(pointsDocumentData)
            .addOnSuccessListener { Log.d(TAG, "Successfully set points!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error setting points!", e) }


    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}