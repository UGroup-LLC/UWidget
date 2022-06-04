package com.kikoproject.uwidget.models.schedules.options

data class ScheduleMorningSettings(
    val startMorningTime: Int,

    val morningTitle: String,
    val morningVisible: Boolean,

    val beforeLesionVisible: Boolean,

    val nextPairVisible: Boolean,

    val showWentTimeVisible: Boolean,
    val showMap: Boolean
)
