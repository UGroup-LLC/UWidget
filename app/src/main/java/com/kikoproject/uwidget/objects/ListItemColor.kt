package com.kikoproject.uwidget.objects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.dialogs.ColorPicker
import com.kikoproject.uwidget.models.schedules.Schedule
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemColor(
    title: String,
    description: String,
    isEnabled: Boolean = false,
    content: (colorValue: Color) -> Unit
) {
    val dialogVisible = remember {
        mutableStateOf(false)
    }

    if(dialogVisible.value){
        ColorPicker(MaterialTheme.colors.primary, dialogVisible)
    }


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                dialogVisible.value = true
            }
    ) {
        val switcherValue = remember {
            mutableStateOf(isEnabled)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = MaterialTheme.colors.surface.copy(0.8f), fontSize = 16.sp)
            Text(
                text = description,
                color = MaterialTheme.colors.surface.copy(0.5f),
                fontSize = 16.sp
            )
        }
        Card(
            modifier = Modifier.size(35.dp, 35.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primary),
        ) {

        }
    }
}