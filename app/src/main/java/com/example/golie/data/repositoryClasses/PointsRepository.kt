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


    fun setPoints(userId: String, points: Int) : Task<Void>{

        val pointsDocumentData = mapOf(
            "points" to points
        )

        return db.collection("users").document(userId).update(pointsDocumentData)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}