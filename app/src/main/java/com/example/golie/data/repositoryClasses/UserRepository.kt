package com.example.golie.data.repositoryClasses

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class UserRepository : dbCursorRepository(){

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun createUser(newUserId: String) : Task<Void> {

        val points = hashMapOf(
            "points" to 0
        )

        return  db.collection("users").document(newUserId).set(points)

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun checkIfUserExists(userId: String): Task<DocumentSnapshot> {

        return  db.collection("users").document(userId).get()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}