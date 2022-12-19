package com.kikoproject.uwidget.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.localdb.MainDataBase
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.defaultScheduleOption
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.navigation.NavigationSetup
import com.kikoproject.uwidget.networking.CheckUserInDB
import com.kikoproject.uwidget.ui.theme.*


@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

@SuppressLint("StaticFieldLeak")
var roomDb: MainDataBase? = null

@SuppressLint("StaticFieldLeak")
lateinit var curUser: User

@SuppressLint("StaticFieldLeak")
lateinit var materialColors: Colors

@SuppressLint("StaticFieldLeak")
var curSchedule: Schedule? = null // Расписание используемое для передачи в разные окна

@SuppressLint("StaticFieldLeak")
var chosenByUserSchedule: Schedule? = null // Расписание используемое пользователем

@SuppressLint("StaticFieldLeak")
var prefs: SharedPreferences? = null

@SuppressLint("StaticFieldLeak")
var options: ScheduleOptions? = null

@SuppressLint("StaticFieldLeak")
var countOfBan = 0
var isJoinBanned = false
var timeUntilBanIslifted: MutableState<Long> = mutableStateOf(0L)
var timer = true

//Const
val days = listOf(
    "Понедельник",
    "Вторник",
    "Среда",
    "Четверг",
    "Пятница",
    "Суббота",
    "Воскресеьне"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomDb = Room.databaseBuilder(
            applicationContext,
            MainDataBase::class.java, "main_database"
        ).allowMainThreadQueries().build()


        setContent {

            if (roomDb!!.optionsDao()
                    .get() != null
            ) { // Это возможно но ? убран чтобы не ебаться ибо инициализация все равно идет в начале и настройки должны быть созданы
                themeAppMode.value = roomDb!!.optionsDao().get().Theme
                systemThemeIsEnabled.value = roomDb!!.optionsDao().get().IsSystemColors
                monetEngineIsEnabled.value = roomDb!!.optionsDao().get().IsMonetEngineEnable
                LaunchAppApplyColors(roomDb!!.optionsDao().get()) // Выставка цветов при запуске
            } else {
                SystemThemeSet()
            }


            UWidgetTheme {
                materialColors = MaterialTheme.colors

                if (roomDb!!.optionsDao()
                        .get() == null
                ) { // Это возможно но ? убран чтобы не ебаться ибо инициализация все равно идет в начале и настройки должны быть созданы
                    val primColor = MaterialTheme.colors.primary
                    val primVarColor = MaterialTheme.colors.primaryVariant
                    val secondaryColor = MaterialTheme.colors.secondary
                    val backgroundColor = MaterialTheme.colors.background
                    val errorColor = MaterialTheme.colors.error
                    val textColor = MaterialTheme.colors.surface

                    roomDb!!.optionsDao().insertOption(
                        GeneralOptions(
                            Colors = listOf(
                                primColor,
                                primVarColor,
                                secondaryColor,
                                backgroundColor,
                                errorColor,
                                textColor
                            ), SchedulesOptions = defaultScheduleOption()
                        )
                    )

                }
                options = roomDb!!.optionsDao().get().SchedulesOptions

                analyticsEnable() // Включение аналитики
                Wait()
                val context = LocalContext.current
                navController = rememberNavController()
                NavigationSetup(navController = navController)
                val state = remember { mutableStateOf(true) }


                prefs = this.getSharedPreferences(
                    context.packageName, Context.MODE_PRIVATE
                )


                CheckUserInDB(
                    context = context,
                    state = state,
                )

            }
        }


    }
}

@Composable
fun Wait() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.TopCenter
    ) {
    }

}

@Preview(showBackground = true)
@Composable
fun AuthPreview() {
    Wait()
}

fun analyticsEnable() {
    Firebase.analytics
}
