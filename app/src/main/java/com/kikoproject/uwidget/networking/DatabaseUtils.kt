package com.kikoproject.uwidget.networking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.DocumentSnapshot
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.DefaultScheduleOption
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.models.schedules.Schedule

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


                        getUserBitmap(
                            userId = document.get("id").toString(),
                            object : AvatarResult {
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

                                    updateAllData(materialColors = materialColors)
                                }
                            })
                    } else {
                        state.value = false
                        navController.navigate(ScreenNav.RegistrationNav.route) // Если пользователь уже залогинен но не создал юзера
                    }
                }
        } else {
            ShowErrorDialog(textError) // Показываем автономный режим
            TODO("Сделать переход в автономный режим")
        }
    } else {
        navController.navigate(ScreenNav.GoogleAuthNav.route) // Если нет аккаунта то отправляем на регистрацию/вход
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
    mySchedulesAdmin.add(schedule)
}

// Получает любое расписание пользователя
fun getNextUserSchedule(): Schedule? {
    if (mySchedulesAdmin.isNotEmpty()) {
        return mySchedulesAdmin[0]
    } else {
        if (mySchedulesUser.isNotEmpty()) {
            return mySchedulesUser[0]
        }
    }

    return null
}

// Обновляет все данные
fun updateAllData(materialColors: Colors){
    getAllUsers(object : UsersResult{
        override fun onResult(users: List<User>) {
            allUsers = users.toMutableList()
        }

        override fun onError(error: Throwable) {
            TODO("Потом сделать вывод ошибки")
        }

    })

    //Получение всех расписаний в этом коде, как мы их получили смотрим есть ли мы в каком то расписании
    //Так же сравниваем локальную и облачную версию бд и если есть обновления в облаке перемещаем в локалку
    getAllSchedules(
        materialColors = materialColors,
        object : ScheduleResult {
            override fun onResult(schedules: List<Schedule>) {
                allSchedules = schedules.toMutableList()

                val schedulesWithCurUser = mutableListOf<Schedule>()
                allSchedules.forEach { schedule ->
                    schedule.UsersID.forEach { userId ->
                        if (curUser.Id == userId) {
                            roomDb.scheduleDao()
                                .insertAll(schedule)
                            mySchedulesUser.add(schedule)
                        }
                    }
                }

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

                // Ищем пользователя в админах бд
                mySchedulesAdmin =
                    roomDb.scheduleDao().getByUserId(curUser.Id)
                        .toMutableList()


                // Поскольку при создании мы не можем получить ID документа, расписание создается с 0 ид и в следующем запуске
                // добавляется ID расписанию, из за этого расписания раздваиваются из за разных ID
                // удаляем все расписания с 0 ID
                roomDb.scheduleDao().getAll().forEach { schedule ->
                    if (schedule.ID == "0") {
                        roomDb.scheduleDao().delete(schedule)
                    }
                }

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

// Выход из БД
fun outFromSchedule(schedule: Schedule, userId: String) {
    mySchedulesUser.remove(schedule)

    val newUsersId = schedule.UsersID.toMutableList()
    newUsersId.remove(userId)
    roomDb.scheduleDao().update(schedule.copy(UsersID = newUsersId))

    db.collection("schedules").document(schedule.ID).update(mapOf(Pair("users_ids", newUsersId)))

    allSchedules.remove(schedule)
    curSchedule = schedule.copy(UsersID = newUsersId)
    allSchedules.add(schedule.copy(UsersID = newUsersId))
}

// Удаление БД из всех БД
fun deleteSchedule(schedule: Schedule) {
    roomDb.scheduleDao().delete(schedule = schedule)
    db.collection("schedules").document(schedule.ID).delete()
    mySchedulesAdmin.remove(schedule)
    mySchedulesUser.remove(schedule)
    allSchedules.remove(schedule)
}

fun createScheduleInDB(schedule: Schedule) {
    if (schedule.Name != "") {
        val user = hashMapOf(
            "admin_id" to schedule.AdminID,
            "users_ids" to schedule.UsersID,
            "category" to schedule.Category,
            "code" to schedule.JoinCode,
            "name" to schedule.Name,
            "schedule" to schedule.Schedule,
            "time" to schedule.Time
        )
        db.collection("schedules").document().set(user)
    }
}

fun enterInSchedule(code: String): Boolean {
    allSchedules.forEach { schedule ->
        val users = schedule.UsersID.toMutableList()

        // Проверяем не состоим ли мы уже в расписании, а так же не являемся ли мы админом этого расписания
        if (schedule.JoinCode == code
            && schedule.AdminID != curUser.Id
            && !schedule.UsersID.contains(curUser.Id)
        ) {
            users.add(curUser.Id)
            // Добавляем пользователя в расписание
            db.collection("schedules").document(schedule.ID).update(
                mapOf(
                    Pair(
                        "users_ids", users
                    )
                )
            )
            curSchedule = schedule
            mySchedulesUser.add(schedule)
            return true
        }
    }
    return false
}

fun getUserBitmap(userId: String, avatarResult: AvatarResult) {
    val imageBytes =
        db.collection("users").document(userId).get().addOnSuccessListener { fields ->
            val imageBytes = Base64.decode(fields.get("image").toString(), Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            avatarResult.onResult(decodedImage)
        }

}

// Получает всех пользователей
fun getAllUsers(usersResult: UsersResult) {

    var usersModel = mutableListOf<User>()

    lateinit var documents: List<DocumentSnapshot>
    db.collection("users").get().addOnSuccessListener {
        documents = it.documents

        documents.forEach { doc ->
            getUserBitmap(doc.get("id").toString(), object : AvatarResult {
                override fun onResult(bitmap: Bitmap) {
                    usersModel.add(
                        User(
                            doc.get("name").toString(),
                            doc.get("surname").toString(),
                            bitmap,
                            doc.get("id").toString()
                        )
                    )
                    usersResult.onResult(usersModel)
                }
            })
        }
    }.addOnFailureListener {
        usersResult.onError(it)
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
                    doc.get("code").toString() as String?,
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

interface UsersResult {
    fun onResult(users: List<User>)
    fun onError(error: Throwable)
}

interface AvatarResult {
    fun onResult(bitmap: Bitmap)
}

