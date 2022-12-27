package com.kikoproject.uwidget.dialogs

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Vibrator
import android.text.format.DateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalTime


/**
 * Открывает диалог получения времени
 * @param timePickerResult возвращает результат диалога в onResult
 *
 * @author Kiko
 */
@Composable
fun OpenTimePicker(
    context: Context,
    timePickerResult: TimePickerResult
) {
    val is24Hour = DateFormat.is24HourFormat(context)
    val haptic = LocalHapticFeedback.current

    WheelTimePicker(
        timeFormat = if (is24Hour) TimeFormat.HOUR_24 else TimeFormat.AM_PM,
        textStyle = MaterialTheme.typography.titleSmall,
        startTime = LocalTime.MIN,
        selectorProperties = WheelPickerDefaults.selectorProperties(
            enabled = true,
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        )
    ) { snappedTime ->
        if(snappedTime != LocalTime.MIN) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            timePickerResult.onResult(time = snappedTime)
        }
    }
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