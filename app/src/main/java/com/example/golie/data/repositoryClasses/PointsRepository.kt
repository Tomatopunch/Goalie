package com.example.golie.data.repositoryClasses

import com.google.android.gms.tasks.Task

class PointsRepository : DbCursorRepository(){

    fun setPoints(userId: String, points: Int) : Task<Void>{
        val pointsDocumentData = mapOf("points" to points)
        return db.collection("users").document(userId).update(pointsDocumentData)
    }
}