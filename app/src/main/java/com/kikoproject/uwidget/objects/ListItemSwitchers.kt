package com.kikoproject.uwidget.objects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.models.schedules.Schedule

@Composable
fun ListItemSwitcher(
    title: String,
    description: String,
    isEnabled: Boolean = false,
    content: (switchValue: Boolean) -> Unit
) {
    Row() {
        val switcherValue = remember {
            mutableStateOf(isEnabled)
        }
        Column(modifier = Modifier.weight(5f)) {
            Text(text = title, color = MaterialTheme.colors.surface.copy(0.8f), fontSize = 16.sp)
            Text(
                text = description,
                color = MaterialTheme.colors.surface.copy(0.5f),
                fontSize = 16.sp
            )
        }
        Switch(
            modifier = Modifier.weight(1f),
            checked = switcherValue.value,
            onCheckedChange = {
                switcherValue.value = !switcherValue.value
                content(switcherValue.value)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedBorderColor = Color.Transparent,
                checkedTrackColor = MaterialTheme.colors.primary.copy(0.25f),
                uncheckedThumbColor = Color.White.copy(0.6f),
                uncheckedBorderColor = Color.Transparent,
                uncheckedTrackColor = Color.Gray,
            )
        )
    }
}