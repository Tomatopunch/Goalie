package com.example.golie.data.dataClasses

data class Reward (
    var title: String,
    var price: Int,
    var id: String = ""
){
    override fun toString() = title
}