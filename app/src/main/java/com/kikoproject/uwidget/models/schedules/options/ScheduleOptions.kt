package com.kikoproject.uwidget.models.schedules.options

data class ScheduleOptions(
    val generalSettings: ScheduleGeneralSettings,
    val scheduleMorningSettings: ScheduleMorningSettings,
    val scheduleDayLesionSettings: ScheduleDayLesionSettings,
    val scheduleDayRestSettings: ScheduleDayRestSettings,
    val scheduleEveningSettings: ScheduleEveningSettings
)
