package com.kikoproject.uwidget.objects

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.ui.theme.themeTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCardCreator(

    //card options
    cardColor: Color = themeTextColor(),
    cardShapeRadius: Dp = 20.dp,
    cardBorderSize: Dp = 1.dp,

    //title options
    titleText: String,
    titleFontSize: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = themeTextColor(),

    //divider options
    dividerColor: Color = themeTextColor(),

    //textfield
    textFieldColor: Color = themeTextColor()
) : MutableState<Int>{
    val states = remember { mutableListOf(mutableStateOf(TextFieldValue(text = ""))) }
    val count = remember {
        mutableStateOf(1)
    }
    val textStates = remember { mutableListOf(mutableStateOf(true)) }

    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(
            cardBorderSize,
            color = cardColor.copy(0.3f)
        ),
        shape = RoundedCornerShape(cardShapeRadius),
        containerColor = cardColor.copy(alpha = 0.05f),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier.padding(10.dp),
                text = titleText,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = titleColor.copy(0.4f)
            )

            Divider(
                color = dividerColor.copy(0.2f),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 10.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                states.forEachIndexed { index, state ->
                    OutlinedTextField(
                        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                        value = state.value,
                        onValueChange = {
                            state.value = it
                            if (it.text.isEmpty()) {
                                textStates[index].value = true
                                if(states.size > 1) {
                                    count.value -= 1
                                    states.removeAt(index)
                                }
                            } else {
                                if(textStates[index].value) {
                                    count.value += 1
                                    textStates[index].value = false
                                    textStates.add(mutableStateOf(true))
                                    states.add(mutableStateOf(TextFieldValue("")))
                                }
                            }
                        },
                        label = {
                            Text(
                                text = "Поле $index",
                                color = textFieldColor.copy(alpha = 0.4f)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        textStyle = TextStyle(color = textFieldColor)
                    )
                }
            }
        }
    }
    return count
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCardCreator(

    cardsInt: Int,

    //card options
    cardColor: Color = themeTextColor(),
    cardShapeRadius: Dp = 20.dp,
    cardBorderSize: Dp = 1.dp,

    //title options
    titleText: String,
    titleFontSize: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = themeTextColor(),

    //divider options
    dividerColor: Color = themeTextColor(),

    //textfield
    textFieldColor: Color = themeTextColor()
) {
    val states = mutableListOf<MutableState<TextFieldValue>>()

    for(cardIndex: Int in 0..cardsInt){
        states.add(remember{mutableStateOf(TextFieldValue(text = ""))})
    }

    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(
            cardBorderSize,
            color = cardColor.copy(0.3f)
        ),
        shape = RoundedCornerShape(cardShapeRadius),
        containerColor = cardColor.copy(alpha = 0.05f),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier.padding(10.dp),
                text = titleText,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = titleColor.copy(0.4f)
            )

            Divider(
                color = dividerColor.copy(0.2f),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 10.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for(cardIndex: Int in 0..cardsInt){
                    OutlinedTextField(
                        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                        value = states[cardIndex].value,
                        onValueChange = {
                            states[cardIndex].value = it
                        },
                        label = {
                            Text(
                                text = "Поле $cardIndex",
                                color = textFieldColor.copy(alpha = 0.4f)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        textStyle = TextStyle(color = textFieldColor)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCreator() {
    ScheduleCardCreator(titleText = "Понедельник")
}