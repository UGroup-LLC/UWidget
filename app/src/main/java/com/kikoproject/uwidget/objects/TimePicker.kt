package com.kikoproject.uwidget.objects
import android.app.Activity
import android.content.Context
import android.text.format.DateFormat.is24HourFormat
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

// Открывает диалог с взятием времени
// isStartLesion - Это что будет отображаться в заголовке пикера
@Composable
fun OpenTimePicker(context: Context, isStartLesion: Boolean){
    val isSystem24Format = is24HourFormat(context)
    val clockFormat = if (isSystem24Format) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H // Формат часов
    val pickerText = if(isStartLesion) "Выберите время начала урока" else "Выберите время конца урока"
    val picker =
        MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setTitleText(pickerText)
            .setMinute(10)
            .build()
    val fragmentActivity = FragmentActivity()
    //picker.show((context as Activity), "")
}