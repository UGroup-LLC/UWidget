package com.kikoproject.uwidget.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R

// Set of Material typography styles to start with
val monserratFamily = FontFamily(
    Font(R.font.monserrat_italic,FontWeight.Normal ,FontStyle.Italic),
    Font(R.font.monsterrat_bold, FontWeight.Bold),
    Font(R.font.monsterrat_extrabold,FontWeight.ExtraBold),
    Font(R.font.monsterrat_semibold,FontWeight.SemiBold),
    Font(R.font.monsterrat_medium,FontWeight.Medium),
    Font(R.font.monsterrat_regular,FontWeight.Normal)
)

val Typography = Typography(

    button = TextStyle(
        fontSize = 16.sp,
        fontFamily = monserratFamily,
        textAlign = TextAlign.Center
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
    ),
    h1 = TextStyle(
        fontSize = 32.sp,
        fontFamily = monserratFamily,
        fontWeight = FontWeight.SemiBold
    ),
    h2 = TextStyle(
        fontSize = 24.sp,
        fontFamily = monserratFamily,
        fontWeight = FontWeight.Medium
    ),
    h6 = TextStyle(
        fontFamily = monserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    ),
    caption = TextStyle(
        fontFamily = monserratFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        letterSpacing = 1.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
     */
)