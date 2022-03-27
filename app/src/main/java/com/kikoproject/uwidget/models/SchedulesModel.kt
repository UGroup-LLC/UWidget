package com.kikoproject.uwidget.models

data class SchedulesModel(
    val Name: String,
    val AdminID: String,
    val UsersID: List<String>,
    val Days: List<Map<String, String>>
)