package com.example.golie.ui.category.goal

data class Goal (
    var title: String,
    var timeSpan: String?,
    var reOccurring: Boolean,
    var points: Int
){
    override fun toString() = title
}