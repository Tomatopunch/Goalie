package com.example.androidlab1

data class ToDo(
    val id: Int, // Val is same as const. Cannot be changed.
    var title: String,
    var content: String
){
    override fun toString() = title
}