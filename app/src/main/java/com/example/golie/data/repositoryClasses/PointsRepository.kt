package com.example.golie.data.repositoryClasses

import android.content.ContentValues
import android.util.Log
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward

class PointsRepository : dbCursorRepository(){

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   fun getPoints(currentUserId: String) : Int {

       var points : Int
       points = -1


       db.collection("users/" + currentUserId + "/points")
           .get()
           .addOnSuccessListener { result ->
               for (document in result) {
                   points = (document.data.getValue("points")).toString().toInt()
                   Log.d(ContentValues.TAG, "Success getting points that were stored in document with id ${document.id} and data ${document.data}")
               }
           }
           .addOnFailureListener { exception ->
               Log.d(ContentValues.TAG, "Error getting points: ", exception)
           }

       return points
   }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun setPoints(currentUserId: String, points: Int){

        val pointsDocumentData = hashMapOf(
            "points" to points
        )

        db.collection("users/" + currentUserId + "/points")
        .add(pointsDocumentData)
            .addOnSuccessListener { documentReference ->

                Log.d(ContentValues.TAG, "Successfully set points to " + points + " in a document with the ID: " + documentReference.id )

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error setting points.", e)
                e.printStackTrace()

            }


    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}