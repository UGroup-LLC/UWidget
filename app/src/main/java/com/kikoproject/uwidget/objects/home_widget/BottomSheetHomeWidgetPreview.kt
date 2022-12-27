package com.kikoproject.uwidget.objects.home_widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope


/**
 * Показать превью расписание, где написаны занятия и их время
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleHomeWidgetPreviewDialog(content: @Composable () -> Unit) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetContent = {
            Column() {
                HomeWidgetPreview()
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),

        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        content()
    }
}

