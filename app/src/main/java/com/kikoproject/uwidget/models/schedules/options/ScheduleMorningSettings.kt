package com.kikoproject.uwidget.models.schedules.options

/**
 * Класс данных отвечающий за настройки расписания в момент утра
 *
 * @author Kiko
 */
data class ScheduleMorningSettings(
    val startMorningTime: Int, // За сколько до начала 1-го занятия начинать утро

    val morningTitle: String, // Текст заголовка
    val morningVisible: Boolean, // Отображение заголовка

    val beforeLesionVisible: Boolean, // Время до 1-ой пары

    val nextPairVisible: Boolean, // Отображение 1-ой пары

    val showWentTimeVisible: Boolean, // Отображений времени поездки-ходьбы до колледжа
    val showMap: Boolean // Отображение карты пути
)
