package com.kikoproject.uwidget.models.schedules.options

/**
 * Класс данных отвечающий за настройки расписания в момент переменной днем
 *
 * @author Kiko
 */
data class ScheduleDayRestSettings(
    val lesionStartTitleVisible: Boolean, // Отображение времени до начала занятия в заголовке
    val nextLesionVisible: Boolean, // Отображение занятия которое будет
    val restCountVisible: Boolean, // Отображение номера переменной
    val homeTimeVisible: Boolean, // Отображения времени до конца всех занятий
    val additionalInfoVisible: Boolean // Отображение дополнительной информации
)
