package com.kikoproject.uwidget.objects
import android.content.Context
import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

// Открывает диалог с взятием времени
// isStartLesion - Это что будет отображаться в заголовке пикера
@Composable
fun OpenTimePicker(context: Context, isStartLesion: Boolean){
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker { time ->
            // Do stuff with java.time.LocalTime object which is passed in
        }
    }
}