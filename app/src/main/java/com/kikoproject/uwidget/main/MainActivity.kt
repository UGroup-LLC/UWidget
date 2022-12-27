package com.kikoproject.uwidget.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
import com.kikoproject.uwidget.BuildConfig
import com.kikoproject.uwidget.localdb.MainDataBase
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.defaultScheduleOption
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.navigation.NavigationSetup
import com.kikoproject.uwidget.networking.CheckUserInDB
import com.kikoproject.uwidget.networking.GitReleases
import com.kikoproject.uwidget.networking.appCheckUpdate
import com.kikoproject.uwidget.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

@SuppressLint("StaticFieldLeak")
var roomDb: MainDataBase? = null

@SuppressLint("StaticFieldLeak")
lateinit var curUser: User

@SuppressLint("StaticFieldLeak")
lateinit var materialColors: ColorScheme

@SuppressLint("StaticFieldLeak")
var curSchedule: Schedule? = null // Расписание используемое для передачи в разные окна

@SuppressLint("StaticFieldLeak")
var chosenByUserSchedule: Schedule? = null // Расписание используемое пользователем

var options by mutableStateOf<ScheduleOptions?>(null)

@SuppressLint("StaticFieldLeak")
var prefs: SharedPreferences? = null

val updateState = mutableStateOf(false) // Есть ли обновление
val gitReleases: MutableState<GitReleases?> = mutableStateOf(GitReleases())

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
            baseContext.applicationContext,
            MainDataBase::class.java, "main_database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        //region Update
        appCheckUpdate(object : Callback<GitReleases> { // Вход
            override fun onResponse(
                call: Call<GitReleases>,
                response: Response<GitReleases>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if((body?.tag_name ?: "") != BuildConfig.VERSION_NAME) {
                        updateState.value = true
                        gitReleases.value = body
                    }
                }
            }

            override fun onFailure(call: Call<GitReleases>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка git api", Toast.LENGTH_SHORT).show()
            }
        })
        //endregion

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
                materialColors = MaterialTheme.colorScheme

                if (roomDb!!.optionsDao()
                        .get() == null
                ) { // Это возможно но ? убран чтобы не ебаться ибо инициализация все равно идет в начале и настройки должны быть созданы
                    val primColor = MaterialTheme.colorScheme.primary
                    val primVarColor = MaterialTheme.colorScheme.primaryContainer
                    val secondaryColor = MaterialTheme.colorScheme.secondary
                    val backgroundColor = MaterialTheme.colorScheme.background
                    val errorColor = MaterialTheme.colorScheme.error
                    val textColor = MaterialTheme.colorScheme.onSurface

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
            .background(MaterialTheme.colorScheme.background),
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
