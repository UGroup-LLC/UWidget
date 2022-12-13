package com.kikoproject.uwidget.objects
import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

// Открывает диалог с взятием времени
// isStartLesion - Это что будет отображаться в заголовке пикера.
/**
 * Открывает диалог получения времени
 * @param timePickerResult возвращает результат диалога в onResult
 *
 * @author Kiko
 */
@Composable
fun OpenTimePicker(timePickerResult: TimePickerResult){
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ок")
            negativeButton("Отменить")
        }
    ) {
        timepicker { time ->
            timePickerResult.onResult(time = time)
        }
    }
}

/**
 * Интерфейс служащий для возврщения результата времени от TimePicker
 */
interface TimePickerResult {
    fun onResult(time: LocalTime)
}