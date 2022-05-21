package com.kikoproject.uwidget.models

data class SchedulesModel(
    val Name: String,
    val AdminID: String,
    val UsersID: List<String>,
    val Schedule: Map<String, MutableList<String>>,
    val Time: List<String>,
    val Category: String
)