package com.example.golie.data.repositoryClasses

import com.google.firebase.firestore.FirebaseFirestore

open class DbCursorRepository {

    val db = FirebaseFirestore.getInstance()

}