package com.kikoproject.uwidget.dialogs

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

/**
 * Открывает диалог получения времени
 * @param titleText Текст диалога
 * @param timePickerResult возвращает результат диалога в onResult
 *
 * @author Kiko
 */
@Composable
fun OpenTimePicker(
    context: Context,
    titleText: String,
    timePickerResult: TimePickerResult,
    range: ClosedRange<LocalTime>
) {
    val dialogState = rememberMaterialDialogState()

    val is24Hour = DateFormat.is24HourFormat(context)

    MaterialDialog(
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(0.4f)),
        backgroundColor = MaterialTheme.colors.background,
        dialogState = dialogState,
        elevation = 0.dp,
        onCloseRequest = {
            dialogState.hide()
            timePickerResult.onClose()
        },
        buttons = {
            positiveButton("Ок")
            negativeButton("Отмена")
        }
    ) {

        timepicker(
            title = titleText, colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colors.secondary,
                inactiveBackgroundColor = MaterialTheme.colors.secondary.copy(0.4f),
                activeTextColor = MaterialTheme.colors.primary,
                inactiveTextColor = MaterialTheme.colors.primary.copy(0.6f),
                inactivePeriodBackground = MaterialTheme.colors.background,
                selectorColor = MaterialTheme.colors.primary.copy(0.6f),
                selectorTextColor = MaterialTheme.colors.primary,
                headerTextColor = MaterialTheme.colors.primary,
                borderColor = MaterialTheme.colors.primary,
            ),
            timeRange = range,
            is24HourClock = is24Hour
        ) { time ->
            dialogState.hide()
            timePickerResult.onResult(time = time)
        }
    }
    dialogState.show()
}

/**
 * Интерфейс служащий для возврщения результата времени от TimePicker
 *
 * onResult при удачном возырате времени из диалога
 * onClose при закрытии диалога
 *
 * @author Kiko
 */
interface TimePickerResult {
    fun onResult(time: LocalTime)
    fun onClose()
}