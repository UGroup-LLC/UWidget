package com.kikoproject.uwidget.objects

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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

/**
 * Подсказка по работе приложения первично отображается в скрытом виде, а затем пользователь
 * может раскрыть подсказку
 *
 * @param title заголовок подсказки
 * @param text основной текст подсказки
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableTextHelper(
    modifier: Modifier = Modifier,
    title: String,
    titleWeight: FontWeight = FontWeight.Medium,
    titleSize: TextUnit = 18.sp,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    textWidthFraction: Float = 0.9f,
    textPadding: Dp = 10.dp,
    fontSize: TextUnit = 12.sp,
    textWeight: FontWeight = FontWeight.Thin,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    cardColor: Color = MaterialTheme.colorScheme.primary
) {
    val expandedState = remember { mutableStateOf(false) }
    val rotateState by animateFloatAsState(targetValue = if (expandedState.value) 180f else 0f)

    Card(
        modifier = modifier.padding(bottom = 5.dp)
            .fillMaxWidth(0.8f)
            .animateContentSize(animationSpec = tween(durationMillis = 50, easing = LinearEasing)),
        border = BorderStroke(
            1.dp,
            color = cardColor
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = { expandedState.value = !expandedState.value },
        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.05f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        color = titleColor,
                        fontSize = titleSize,
                        fontWeight = titleWeight,
                        textAlign = TextAlign.Center
                    )
                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = { expandedState.value = !expandedState.value },
                            modifier = Modifier
                                .rotate(rotateState)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = "Arrow dropdown",
                                tint = titleColor
                            )
                        }
                    }
                }
            }
            if (expandedState.value) {
                Divider(
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