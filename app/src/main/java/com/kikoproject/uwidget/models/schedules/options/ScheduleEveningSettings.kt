package com.kikoproject.uwidget.models.schedules.options

/**
 * Класс данных отвечающий за настройки расписания в момент вечера
 *
 * @author Kiko
 */
data class ScheduleEveningSettings(
    val eveningTitleText: String, // Текст отображаемый в заголовке вечером
    val eveningTitleVisible: Boolean, // Отображение заголовка

    val allScheduleVisible: Boolean, // Отображение всех занятий на следующий день
    val additionalInfoVisible: Boolean // Отображение дополнительной информации
)
