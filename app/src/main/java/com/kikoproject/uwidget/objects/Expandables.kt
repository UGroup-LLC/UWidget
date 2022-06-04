package com.kikoproject.uwidget.objects

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.schedules.DrawAllSchedulesInCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableTextHelper(
    title: String,
    titleWeight: FontWeight = FontWeight.Medium,
    titleSize: TextUnit = 18.sp,
    titleColor: Color = MaterialTheme.colors.surface,
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    textWidthFraction: Float = 0.9f,
    textPadding: Dp = 10.dp,
    fontSize: TextUnit = 12.sp,
    textWeight: FontWeight = FontWeight.Thin,
    textColor: Color = MaterialTheme.colors.surface,
    cardColor: Color = MaterialTheme.colors.primary
) {
    val expandedState = remember { mutableStateOf(false) }
    val rotateState by animateFloatAsState(targetValue = if (expandedState.value) 180f else 0f)

    Card(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .fillMaxWidth(0.8f)
            .animateContentSize(animationSpec = tween(durationMillis = 50, easing = LinearEasing)),
        border = BorderStroke(
            1.dp,
            color = cardColor
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = { expandedState.value = !expandedState.value },
        containerColor = cardColor.copy(alpha = 0.05f),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(6f),
                    color = titleColor,
                    fontSize = titleSize,
                    fontWeight = titleWeight,
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { expandedState.value = !expandedState.value },
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotateState)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = "Arrow dropdown",
                        tint = titleColor,
                        modifier = Modifier.weight(2f)
                    )
                }
            }
            if (expandedState.value) {
                androidx.compose.material.Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 5.dp), color = textColor.copy(alpha = 0.2f)
                )

                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth(textWidthFraction)
                        .padding(textPadding),
                    textAlign = textAlign,
                    fontSize = fontSize,
                    fontWeight = textWeight,
                    color = textColor.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableScheduleCategory(
    title: String,
    schedule: MutableList<Schedule>,
    textColor: Color
) {
    val expandedState = remember { mutableStateOf(false) }
    val rotateState by animateFloatAsState(targetValue = if (expandedState.value) 180f else 0f)

    Card(
        modifier = Modifier
            .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 50, easing = LinearEasing)),
        border = BorderStroke(
            1.dp,
            color = MaterialTheme.colors.primary
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = { expandedState.value = !expandedState.value },
        containerColor = MaterialTheme.colors.primary.copy(alpha = 0.3f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(6f)
                        .padding(5.dp),
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { expandedState.value = !expandedState.value },
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotateState)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = "Arrow dropdown",
                        tint = textColor,
                        modifier = Modifier.weight(2f)
                    )
                }
            }
            if (expandedState.value) {
                DrawAllSchedulesInCategory(schedule, textColor, title)
            }
        }
    }

}