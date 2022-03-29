package com.kikoproject.uwidget.schedules

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.models.SchedulesModel
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.ScheduleResult
import com.kikoproject.uwidget.networking.getAllSchedules
import com.kikoproject.uwidget.objects.ExpandableScheduleCategory
import com.kikoproject.uwidget.ui.theme.Shapes
import com.kikoproject.uwidget.ui.theme.themeTextColor
import kotlin.math.exp

@Composable
fun ChooseSchedule() {
    val loadingState = remember { mutableStateOf(true) }

    if (loadingState.value) {
        ShowLoadingDialog(state = loadingState)
    }

    val textColor = themeTextColor()
    val context = LocalContext.current

    val categoryNames = remember { mutableListOf<String>() }
    val notCategorySchedulesModel = remember { mutableListOf<SchedulesModel>() }
    val withCategorySchedulesModel = remember { mutableListOf<SchedulesModel>() }


    getAllSchedules(object : ScheduleResult {
        override fun onResult(schedules: List<SchedulesModel>) {

            notCategorySchedulesModel.clear()
            withCategorySchedulesModel.clear()
            categoryNames.clear()

            loadingState.value = false
            schedules.forEach { schedule ->
                if (schedule.Category == "") {
                    notCategorySchedulesModel.add(schedule)
                } else {
                    if(!categoryNames.contains(schedule.Category)) { // Если этой категории еще нет
                        categoryNames.add(schedule.Category)
                    }
                    withCategorySchedulesModel.add(schedule)
                }
            }
        }

        override fun onError(error: Throwable) {
            error.message?.let { Log.e(TAG, it) }
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedButton(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(0.9f),
                onClick = { navController.navigate(ScreenNav.AddScheduleNav.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colors.primary.copy(
                        alpha = 0.2f
                    )
                ),
                border = BorderStroke(
                    1.dp,
                    color = MaterialTheme.colors.primary
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add schedule",
                        tint = textColor
                    )
                    Text(
                        "Добавить новое расписание",
                        color = textColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Divider(color = textColor.copy(alpha = 0.3f), modifier = Modifier.padding(20.dp))
            Text(
                "Публичные расписания",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp),
                fontWeight = FontWeight.Bold
            )

            if (withCategorySchedulesModel.size != 0) {
                LazyColumn() {
                    items(categoryNames) { name ->
                        ExpandableScheduleCategory(
                            title = name,
                            schedulesModel = withCategorySchedulesModel,
                            textColor = textColor
                        )
                    }
                }
            }
            if (notCategorySchedulesModel.size != 0) {
                FlexibleSchedulesButtons(notCategorySchedulesModel, textColor)
            }
        }
    }
}

@Composable
private fun FlexibleSchedulesButtons(
    schedulesModel: MutableList<SchedulesModel>,
    textColor: Color
) {
    LazyColumn() {
        items(schedulesModel) { schedule ->
            OutlinedButton(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(0.9f),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colors.primary.copy(
                        alpha = 0.2f
                    )
                ),
                border = BorderStroke(
                    1.dp,
                    color = MaterialTheme.colors.primary
                ),
            ) {
                Text(
                    schedule.Name,
                    color = textColor,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChooseSchedule() {
    ChooseSchedule()
}

@Composable
fun DrawAllSchedulesInCategory(
    schedulesModel: MutableList<SchedulesModel>,
    textColor: Color,
    categoryName: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(10.dp), color = textColor.copy(alpha = 0.4f)
        )
        schedulesModel.forEach { schedule ->
            if(schedule.Category == categoryName) {
                OutlinedButton(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxWidth(0.9f),
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colors.primary.copy(
                            alpha = 0.2f
                        )
                    ),
                    border = BorderStroke(
                        1.dp,
                        color = MaterialTheme.colors.primary
                    ),
                ) {
                    Text(
                        schedule.Name,
                        color = textColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}