package com.kikoproject.uwidget.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.localdb.MainDataBase
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.NavigationSetup
import com.kikoproject.uwidget.networking.CheckUserInDB
import com.kikoproject.uwidget.ui.theme.UWidgetTheme

@SuppressLint("StaticFieldLeak")
lateinit var navController: NavHostController
@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore
@SuppressLint("StaticFieldLeak")
lateinit var roomDb: MainDataBase
@SuppressLint("StaticFieldLeak")
lateinit var curUser: User
@SuppressLint("StaticFieldLeak")
var mySchedulesAdmin: MutableList<Schedule> = mutableListOf()
@SuppressLint("StaticFieldLeak")
var mySchedulesUser: MutableList<Schedule> = mutableListOf()
@SuppressLint("StaticFieldLeak")
lateinit var curSchedule: Schedule
@SuppressLint("StaticFieldLeak")
lateinit var allSchedules: MutableList<Schedule>
@SuppressLint("StaticFieldLeak")
lateinit var allUsers: MutableList<User>

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UWidgetTheme {
                analyticsEnable() // Включение аналитики
                Wait()
                val context = LocalContext.current
                navController = rememberNavController()
                NavigationSetup(navController = navController)
                val state = remember { mutableStateOf(true) }

                roomDb = Room.databaseBuilder(
                    applicationContext,
                    MainDataBase::class.java, "main_database"
                ).allowMainThreadQueries().build()

                CheckUserInDB(
                    context = context,
                    state = state,
                    textError = "Автономный режим",
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
    val analytics = Firebase.analytics
}