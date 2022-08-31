package com.kikoproject.uwidget.objects

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedCard(
    textColor: Color,
    text: String,
    textSize: TextUnit = 12.sp,
    spacing: TextUnit = 3.sp,
    modifier: Modifier = Modifier
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
            color = textColor.copy(0.6f),
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 2.5.dp
            )
        )
    }
}