package com.kikoproject.uwidget.objects.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties


@Composable
fun StandardBottomSheet(
    dialogVisibleState: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = {
            dialogVisibleState.value = false
        },
        properties = BottomSheetDialogProperties()
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(modifier = Modifier.clip(CircleShape)) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        thickness = 3.dp
                    )
                }
                content()
            }
        }
    }
}

