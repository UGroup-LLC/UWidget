package com.kikoproject.uwidget.networking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.model.Document
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.DefaultScheduleOption
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.*

@Composable
fun CheckUserInDB(
    context: Context,
    state: MutableState<Boolean>,
    account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context),
    textError: String
) {
    navController.popBackStack()
    val materialColors = MaterialTheme.colors

    if (account != null) {
        if (isOnline(context)) { // Проверка на онлайн
            ShowLoadingDialog(state)
            // поиск аккаунта в firebase
            db.collection("users").document(account.id.toString()).get()
                .addOnCompleteListener {
                    val document = it.result
                    if (document.exists()) { // если акк есть


                        getUserBitmap(account = account, object : AvatarResult {
                            override fun onResult(bitmap: Bitmap) {
                                curUser = User(
                                    document.get("name").toString(),
                                    document.get("surname").toString(),
                                    bitmap,
                                    document.get("id").toString()
                                )

                                state.value = false

                                // Проверит есть ли аккаунт в локальной БД и занесет если нет
                                insertAccountInRoom(curUser)

                                //Получение всех расписаний в этом коде, как мы их получили смотрим есть ли мы в каком то расписании
                                //Так же сравниваем локальную и облачную версию бд и если есть обновления в облаке перемещаем в локалку
                                getAllSchedules(
                                    materialColors = materialColors,
                                    object : ScheduleResult {
                                        override fun onResult(schedules: List<Schedule>) {
                                            allSchedules = schedules.toMutableList()

                                            schedules.forEach { schedule ->
                                                val currentScheduleInRoomDB =
                                                    roomDb.scheduleDao().getWithId(schedule.ID)
                                                if (currentScheduleInRoomDB != null) {
                                                    roomDb.scheduleDao()
                                                        .insertAll(schedule.copy(Options = currentScheduleInRoomDB.Options))
                                                } else {
                                                    roomDb.scheduleDao().insertAll(schedule)
                                                }
                                            }

                                            curSchedules = roomDb.scheduleDao().getByAdminId(curUser.Id).toMutableList()

                                            navController.navigate(ScreenNav.ScheduleChooseNav.route)
                                            schedules.forEach { schedule ->
                                                if (schedule.AdminID == curUser.Id) {
                                                    navController.popBackStack()
                                                    navController.navigate(ScreenNav.Dashboard.route)
                                                    return
                                                }
                                            }
                                        }

                                        override fun onError(error: Throwable) {
                                            navController.navigate(ScreenNav.ScheduleChooseNav.route)
                                        }
                                    })
                            }
                        })
                    } else {
                        state.value = false
                        navController.navigate(ScreenNav.RegistrationNav.route) // Если пользователь уже залогинен но не создал юзера
                    }
                }
        } else {
            ShowErrorDialog(textError)
        }
    } else {
        navController.navigate(ScreenNav.GoogleAuthNav.route) // Если нет то гуглимся
    }
}

fun insertAccountInRoom(user: User?) {
    if (user != null) {
        if (roomDb.userDao().findById(user.Id) == null) {
            roomDb.userDao().insertUser()
        }
    }
}

fun createScheduleInRoomDB(schedule: Schedule) {
    roomDb.scheduleDao().insertAll(schedule)
    allSchedules.add(schedule)
    curSchedules.add(schedule)
}

fun createScheduleInDB(schedule: Schedule) {
    if (schedule.Name != "") {
        val user = hashMapOf(
            "admin_id" to schedule.AdminID,
            "users_ids" to schedule.UsersID,
            "category" to schedule.Category,
            "name" to schedule.Name,
            "schedule" to schedule.Schedule,
            "time" to schedule.Time
        )
        db.collection("schedules").document().set(user)
    }
}

fun getUserBitmap(account: GoogleSignInAccount, avatarResult: AvatarResult) {
    if (account.id != null) {
        val imageBytes =
            db.collection("users").document(account.id!!).get().addOnSuccessListener { fields ->
                val imageBytes = Base64.decode(fields.get("image").toString(), Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                avatarResult.onResult(decodedImage)
            }
    }
}

fun getAllSchedules(materialColors: Colors, scheduleResult: ScheduleResult) {

    var schedulesModel = mutableListOf<Schedule>()


    lateinit var documents: List<DocumentSnapshot>
    db.collection("schedules").get().addOnSuccessListener {
        documents = it.documents

        documents.forEach { doc ->
            schedulesModel.add(
                Schedule(
                    doc.id,
                    doc.get("name").toString(),
                    doc.get("admin_id").toString(),
                    doc.get("users_ids") as List<String>,
                    doc.get("schedule") as Map<String, MutableList<String>>,
                    doc.get("time") as List<String>,
                    doc.get("category").toString(),
                    DefaultScheduleOption(materialColors = materialColors)
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
    }
}


interface ScheduleResult {
    fun onResult(schedules: List<Schedule>)
    fun onError(error: Throwable)
}

interface AvatarResult {
    fun onResult(bitmap: Bitmap)
}

