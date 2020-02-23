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

    // Funkar
   fun getPoints(currentUserId: String) : Task<DocumentSnapshot> {

       return db.collection("users/" + currentUserId + "/points").document("currentPoints").get()

   }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Funkar
    fun setPoints(currentUserId: String, points: Int) : Task<Void>{

        val pointsDocumentData = hashMapOf(
            "points" to points
        )


        return db.collection("users/" + currentUserId + "/points").document("currentPoints").set(pointsDocumentData)

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}