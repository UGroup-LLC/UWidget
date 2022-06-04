package com.kikoproject.uwidget.schedules

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kikoproject.uwidget.*
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.dialogs.ScheduleDialogSelector
import com.kikoproject.uwidget.dialogs.ShowSearchSelector
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.models.schedules.DefaultScheduleOption
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.PermissionNav
import com.kikoproject.uwidget.networking.createScheduleInDB
import com.kikoproject.uwidget.networking.createScheduleInRoomDB
import com.kikoproject.uwidget.objects.ExpandableTextHelper
import com.kikoproject.uwidget.objects.IncreaseButtons
import com.kikoproject.uwidget.objects.ScheduleCardCreator
import com.kikoproject.uwidget.ui.theme.Typography
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSchedule() {
    val textColor = MaterialTheme.colors.surface
    val nameState = remember { mutableStateOf(TextFieldValue(text = curSchedule.Name)) }
    val categoryState = remember { mutableStateOf(TextFieldValue(text = curSchedule.Category)) }
    val schedulesState = remember { mutableStateOf(mutableListOf<MutableList<String>>()) }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val timeCount = remember { mutableStateOf(0) }
    var timeState = mutableListOf<MutableState<TextFieldValue>>()
    val materialColor = MaterialTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Редактирование расписания",
                    fontFamily = FontFamily(Font(R.font.gogh)),
                    color = textColor,
                    fontSize = 24.sp
                )



                Divider(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(0.6f), color = textColor.copy(alpha = 0.4f)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(0.8f),
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Введите имя расписания",
                            color = textColor.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = textColor)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(0.8f),
                    value = categoryState.value,
                    onValueChange = { categoryState.value = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Введите имя категории",
                            color = textColor.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = textColor)
                )
            }
            item {
                ExpandableTextHelper(
                    cardColor = textColor.copy(alpha = 0.2f),
                    titleSize = 12.sp,
                    titleColor = textColor.copy(alpha = 0.25f),
                    fontSize = 14.sp,
                    title = "Что такое категория?",
                    text = "Если у вас множество расписаний, вы можете сгруппировать их в одну категорию (Например: Колледж моды - 1 курс), если вам не нужна категория оставьте поле пустым"
                )
                val primaryColor = MaterialTheme.colors.primary

                Text(
                    text = "Способ получения расписания",
                    color = textColor,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                val scheduleGetMode = IncreaseButtons(
                    texts = listOf("Ручное заполнение", "Получение с сайта"),
                    inactiveColor = textColor.copy(0.1f),
                    roundStrength = 30f,
                    activeColor = primaryColor.copy(0.5f),
                    fontSize = 11.5.sp
                )
                val days = listOf(
                    "Понедельник",
                    "Вторник",
                    "Среда",
                    "Четверг",
                    "Пятница",
                    "Суббота",
                    "Воскресеьне"
                )

                val count = remember { mutableListOf<MutableState<Int>>() }

                Spacer(modifier = Modifier.padding(10.dp))

                if (scheduleGetMode == 0) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        border = BorderStroke(
                            1.dp,
                            color = textColor.copy(0.2f)
                        ),
                        shape = RoundedCornerShape(25.dp),
                        containerColor = textColor.copy(alpha = 0.03f)
                    ) {

                        Box(
                            Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .fillMaxWidth(0.86f),
                            contentAlignment = Alignment.Center
                        ) {
                            ExpandableTextHelper(
                                cardColor = textColor.copy(alpha = 0.2f),
                                titleSize = 12.sp,
                                titleColor = textColor.copy(alpha = 0.25f),
                                fontSize = 14.sp,
                                title = "Как заполнить расписание?",
                                text = "Заполните ваше расписание на каждый день, если у вас есть окна между парами/уроками поставьте пробел для создания доп. поля. Поля будут создаваться бесконечно, поэтому последнее поле всегда будет пустым."
                            )
                        }
                        count.clear()
                        schedulesState.value.clear()
                        days.forEach { day -> // Тут идет получение всего что написал юзер

                            val card = ScheduleCardCreator(titleText = day)
                            count.add(mutableStateOf(card.size))
                            val scheduleForDay = mutableListOf<String>()

                            card.forEach { item ->
                                if (item.value.text != "") {
                                    scheduleForDay.add(item.value.text)
                                }
                            }

                            schedulesState.value.add(scheduleForDay)
                        }

                        var tempMax = 0
                        count.forEach { cout ->
                            if (tempMax < cout.value - 2) {
                                tempMax = cout.value - 2
                            }
                        }
                        timeCount.value = tempMax
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        border = BorderStroke(
                            1.dp,
                            color = textColor.copy(0.2f)
                        ),
                        shape = RoundedCornerShape(25.dp),
                        containerColor = textColor.copy(alpha = 0.03f)
                    ) {

                        Box(
                            Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .fillMaxWidth(0.86f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    containerColor = textColor.copy(0.1f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        "ALPHA",
                                        letterSpacing = 3.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 12.sp,
                                        color = textColor.copy(0.6f),
                                        modifier = Modifier.padding(
                                            horizontal = 15.dp,
                                            vertical = 2.5.dp
                                        )
                                    )
                                }
                                ExpandableTextHelper(
                                    cardColor = textColor.copy(alpha = 0.2f),
                                    titleSize = 12.sp,
                                    titleColor = textColor.copy(alpha = 0.25f),
                                    fontSize = 14.sp,
                                    title = "Как настроить парсер?",
                                    text = "Если у вашего завдения имеется расписание, вы можете попробовать вытащить расписание с сайта заведения. Для этого вам необходимо ввести ссылку на расписание ниже. "
                                )
                                val urlState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                val pairsState = remember { mutableStateOf(TextFieldValue("")) }
                                val sluState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                val sldState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                val sruState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                FastOutlineTextField("Сайт для парсинга", urlState, textColor)
                                if (urlState.value.text.contains(".")) {
                                    FastOutlineTextField(
                                        "Сколько пар/уроков на сайте в день",
                                        pairsState,
                                        textColor
                                    )
                                }
                                if (urlState.value.text.contains(".") && pairsState.value.text != "") {
                                    ExpandableTextHelper(
                                        cardColor = textColor.copy(alpha = 0.2f),
                                        titleSize = 12.sp,
                                        titleColor = textColor.copy(alpha = 0.25f),
                                        fontSize = 14.sp,
                                        title = "Что дальше?",
                                        text = "Далее вам необходимо ввести селекторы на пару. Если вы не знаете селектор, то нажмите на значок лупу и скопируйте название вашего предмета"
                                    )
                                    FastOutlineTextField(
                                        "Понедельник - 1 пара",
                                        sluState,
                                        textColor,
                                        Icons.Rounded.Search,
                                        urlState.value.text
                                    )
                                    FastOutlineTextField(
                                        "Понедельник - 2 пара",
                                        sldState,
                                        textColor,
                                        Icons.Rounded.Search,
                                        urlState.value.text
                                    )
                                    FastOutlineTextField(
                                        "Вторник - 1 пара",
                                        sruState,
                                        textColor,
                                        Icons.Rounded.Search,
                                        urlState.value.text
                                    )
                                }
                                val scope = rememberCoroutineScope()

                                Button(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .fillMaxWidth(0.9f),
                                    onClick = {

                                        val firstDivider = getSelectorDivider(
                                            sluState.value.text,
                                            sldState.value.text
                                        )
                                        val secondDivider = getSelectorDivider(
                                            sluState.value.text,
                                            sruState.value.text
                                        )

                                        val tmpTimeCount = pairsState.value.text.toIntOrNull()
                                        if (tmpTimeCount != null) {
                                            timeCount.value = tmpTimeCount - 1
                                        }

                                        getSchedule(
                                            scope,
                                            url = urlState.value.text,
                                            sluState.value.text,
                                            firstDivider,
                                            secondDivider,
                                            pairsState.value.text.toInt(),
                                            object : ScheduleGetter {
                                                override fun onResult(schedules: MutableList<MutableList<String>>) {
                                                    schedulesState.value = schedules
                                                }
                                            }
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colors.primary.copy(
                                            alpha = 0.5f
                                        )
                                    ),
                                ) {
                                    Text(
                                        text = "Предпросмотр",

                                        )
                                }
                            }
                        }
                    }
                }

                if (timeCount.value >= 0) {
                    timeState = ScheduleCardCreator(cardsInt = timeCount.value, titleText = "Время")
                }
            }
            item {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth(0.9f),
                    onClick = {
                        val scheduleValue = schedulesState.value
                        val tempMap = mutableMapOf<String, MutableList<String>>()
                        scheduleValue.forEachIndexed { index, value ->
                            tempMap.set(index.toString(), value)
                        }
                        val tempTimeState = mutableListOf<String>()
                        timeState.forEach { item ->
                            tempTimeState.add(item.value.text)
                        }

                        var allSchedulesNull = 0
                        schedulesState.value.forEach { schedule ->
                            if (schedule.size == 0) {
                                allSchedulesNull++
                            }
                            if (allSchedulesNull == 7) {
                                Toast.makeText(
                                    context,
                                    "Ошибка, ни в одном из полей расписания ничего не введенно",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@Button
                            }
                        }


                        timeState.forEach { time ->
                            if (
                                !time.value.text.contains(":")
                                && time.value.text.filter { it.isDigit() }.length != 4
                            ) {
                                Toast.makeText(
                                    context,
                                    "Ошибка, время введенно некорректно, пример: 09:08",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@Button
                            }
                        }

                        if (scheduleValue.isNotEmpty()) {
                            if (nameState.value.text.filter { !it.isWhitespace() } != "") {
                                val adminId = GoogleSignIn.getLastSignedInAccount(context)
                                if (adminId?.id != null) {
                                    val schedule = Schedule(
                                        "0",
                                        nameState.value.text,
                                        adminId.id!!,
                                        listOf(""),
                                        tempMap,
                                        tempTimeState,
                                        categoryState.value.text,
                                        DefaultScheduleOption(materialColor)
                                    )
                                    createScheduleInRoomDB(schedule)
                                    createScheduleInDB(
                                        schedule = schedule
                                    )
                                    navController.navigate(PermissionNav.BackgroundActivity.route)
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Ошибка, пустое название",
                                    Toast.LENGTH_LONG
                                ).show()
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        } else {
                            Toast.makeText(context, "Ошибка, пустое расписание", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colors.primary.copy(
                            alpha = 0.5f
                        )
                    ),
                ) {
                    Text(
                        text = "Изменить расписание",
                        color = textColor,
                        style = Typography.button
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth(0.9f),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red.copy(
                            alpha = 0.5f
                        )
                    )
                ){
                    Text(
                        text = "Удалить расписание",
                        color = Color.White,
                        style = Typography.button
                    )
                }
            }
        }
    }
}
