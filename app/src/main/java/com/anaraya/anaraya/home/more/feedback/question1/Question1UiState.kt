package com.anaraya.anaraya.home.more.feedback.question1

data class Question1UiState(
    val angry:Boolean = false,
    val sad:Boolean = false,
    val normal:Boolean = false,
    val satisfy:Boolean = false,
    val happy:Boolean = false,
    val rate:Int = 0,
)
