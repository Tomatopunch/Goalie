package com.example.golie.data.dataClasses

data class Goal (
    var title: String = "",
    var timeSpan: String? = "",
    var reoccurring: Boolean = false,
    var points: Int = -1,
    var id: String = ""
){
    override fun toString() = title
}