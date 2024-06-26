package com.kikoproject.uwidget.objects.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedCard(
    modifier: Modifier = Modifier,
    textColor: Color,
    text: String,
    textSize: TextUnit = 12.sp,
    spacing: TextUnit = 3.sp,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = textColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Text(
            text,
            letterSpacing = spacing,
            fontWeight = FontWeight.ExtraBold,
            fontSize = textSize,
            style = MaterialTheme.typography.labelMedium,
            color = textColor.copy(0.6f),
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 2.5.dp
            )
        )
    }
}