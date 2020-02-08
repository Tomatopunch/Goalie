package com.example.golie.ui.category.goal

data class Goal (
    val id: Int,
    var title: String,
    var timeSpan: String?,
    var reOccurring: Boolean,
    var points: Int
){
    override fun toString() = title
}