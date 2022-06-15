package com.kikoproject.uwidget.models.schedules.options

/**
 * Класс данных отвечающий за настройки расписания в момент занятия днем
 *
 * @author Kiko
 */
data class ScheduleDayLesionSettings(
    val lesionEndTitleVisible: Boolean, // Отображение времени до конца занятия в заголовке
    val nextLesionVisible: Boolean, // Отображение следющего занятия
    val lesionCountVisible: Boolean, // Отображение номера текущего занятия
    val homeTimeVisible: Boolean, // Отображения времени до конца всех занятий
    val additionalInfoVisible: Boolean // Отображение дополнительной информации
)
