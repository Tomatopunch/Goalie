package com.example.golie.data.dataClasses

data class Goal (
    var title: String,
    var timeSpan: String?,
    var reoccurring: Boolean,
    var points: Int,
    var id: String = ""
){
    override fun toString() = title
}