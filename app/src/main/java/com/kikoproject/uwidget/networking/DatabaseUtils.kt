package com.kikoproject.uwidget.networking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            val errorState = remember{mutableStateOf(false)}
            val errorMessage = remember{mutableStateOf("")}
            if(errorState.value){
                ShowErrorDialog(text = errorMessage.value,false)
            }
            // поиск аккаунта в firebase
            db.collection("users").document(account.id.toString()).get()
                .addOnCompleteListener {
                    try {
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

                                        updateAllData(
                                            user = curUser,
                                            materialColors = materialColors,
                                            content = { myScheduleUser, myScheduleAdmin ->

                                                //********** Записываем в локальную БД **********

                                                myScheduleUser.forEach { schedule ->
                                                    roomDb.scheduleDao().insertAll(schedule)
                                                }
                                                myScheduleAdmin.forEach { schedule ->
                                                    roomDb.scheduleDao().insertAll(schedule)
                                                }


                                                // Поскольку при создании мы не можем получить ID документа, расписание создается с 0 ид и в следующем запуске
                                                // добавляется ID расписанию, из за этого расписания раздваиваются из за разных ID
                                                // удаляем все расписания с 0 ID
                                                roomDb.scheduleDao().getAll().forEach { schedule ->
                                                    if (schedule.ID == "0") {
                                                        roomDb.scheduleDao().delete(schedule)
                                                    }
                                                }

                                                navController.navigate(ScreenNav.Dashboard.route)
                                            })
                                    }
                                })
                        } else {
                            state.value = false
                            navController.navigate(ScreenNav.RegistrationNav.route) // Если пользователь уже залогинен но не создал юзера
                        }
                    }
                    catch (exeption: Exception){
                        // Если пришли пиздарики
                        errorMessage.value = exeption.message.toString()
                        errorState.value = true
                    }
                }
                .addOnFailureListener {
                    // Если пришли пиздарики
                    errorMessage.value = it.message.toString()
                    errorState.value = true
                }
        } else {
            ShowErrorDialog(textError,true) // Показываем автономный режим
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
}

// Получает любое расписание пользователя
fun getNextUserSchedule(
    mySchedulesAdmin: List<Schedule>,
    mySchedulesUser: List<Schedule>
): Schedule? {
    if (mySchedulesAdmin.isNotEmpty()) {
        return mySchedulesAdmin[0]
    } else {
        if (mySchedulesUser.isNotEmpty()) {
            return mySchedulesUser[0]
        }
    }
    return null
}


// Подгружает всех юзеров кто состоит в указанном расписании
@Composable
fun MembersOnlineContent(
    content: @Composable (users: List<User>) -> Unit,
    schedule: Schedule,
) {
    val showContent = remember { mutableStateOf(false) }
    var scheduleUsers = listOf<User>()

    if (showContent.value) {
        content(scheduleUsers)
    }
    else{
        LinearProgressIndicator(
            trackColor = MaterialTheme.colors.primaryVariant,
            color = MaterialTheme.colors.primary
        )
    }

    getScheduleUsers(schedule = schedule, object : UsersResult {
        override fun onResult(users: List<User>) {
            showContent.value = true
        }

        override fun onError(error: Throwable) {
            TODO("Not yet implemented")
        }

    })
}

// Прогружает расписания юзера пока не прогрузило, показывает loading ui
@Composable
fun OnlineContent(
    content: @Composable (mySchedulesUser: List<Schedule>, mySchedulesAdmin: List<Schedule>) -> Unit,
    user: User,
) {
    val showContent = remember { mutableStateOf(false) }
    var mySchedulesUser = listOf<Schedule>()
    var mySchedulesAdmin = listOf<Schedule>()

    if (showContent.value) {
        content(mySchedulesUser, mySchedulesAdmin)
    } else {
        LinearProgressIndicator(
            trackColor = MaterialTheme.colors.primaryVariant,
            color = MaterialTheme.colors.primary
        )
    }

    getAdminSchedules(
        user = user,
        scheduleResult = object : ScheduleResult {
            override fun onResult(mSchedulesUser: List<Schedule>) {
                getUserSchedules(
                    user = user,
                    scheduleResult = object : ScheduleResult {
                        override fun onResult(mSchedulesAdmin: List<Schedule>) {
                            mySchedulesUser = mSchedulesUser
                            mySchedulesAdmin = mSchedulesAdmin
                        }

                        override fun onError(error: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
            }

            override fun onError(error: Throwable) {
                TODO("Not yet implemented")
            }

        })
}

// Прогружает расписания без ui
fun updateAllData(
    content: (mySchedulesUser: List<Schedule>, mySchedulesAdmin: List<Schedule>) -> Unit,
    user: User,
    materialColors: Colors
) {
    getAdminSchedules(
        user = user,
        scheduleResult = object : ScheduleResult {
            override fun onResult(mSchedulesUser: List<Schedule>) {
                getUserSchedules(
                    user = user,
                    scheduleResult = object : ScheduleResult {
                        override fun onResult(mySchedulesAdmin: List<Schedule>) {
                            content(mSchedulesUser, mySchedulesAdmin)
                        }

                        override fun onError(error: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
            }

            override fun onError(error: Throwable) {
                TODO("Not yet implemented")
            }

        })
}


// Выход из БД
fun outFromSchedule(schedule: Schedule, userId: String) {
    val newUsersId = schedule.UsersID.toMutableList()
    newUsersId.remove(userId)
    roomDb.scheduleDao().update(schedule.copy(UsersID = newUsersId))

    db.collection("schedules").document(schedule.ID).update(mapOf(Pair("users_ids", newUsersId)))

    curSchedule = schedule.copy(UsersID = newUsersId)
}

// Удаление БД из всех БД
fun deleteSchedule(schedule: Schedule) {
    roomDb.scheduleDao().delete(schedule = schedule)
    db.collection("schedules").document(schedule.ID).delete()
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

fun enterInSchedule(code: String, enterInScheduleResult: EnterInScheduleResult) {
    db.collection("schedules")
        .whereEqualTo("code", code)
        .whereNotEqualTo("admin_id", curUser.Id)
        .get().addOnSuccessListener {
            val doc = it.documents[0]
            var schedule = Schedule(
                doc.id,
                doc.get("name").toString(),
                doc.get("admin_id").toString(),
                doc.get("users_ids") as List<String>,
                doc.get("schedule") as Map<String, MutableList<String>>,
                doc.get("code").toString() as String?,
                doc.get("time") as List<String>,
                doc.get("category").toString(),
                DefaultScheduleOption()
            )

            val users = schedule.UsersID.toMutableList()

            // Проверяем не состоим ли мы уже в расписании, а так же не являемся ли мы админом этого расписания
            if (!schedule.UsersID.contains(curUser.Id)
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
                enterInScheduleResult.onResult(true)
            }
        }.addOnFailureListener {
            enterInScheduleResult.onResult(false)
        }
}

fun getUserBitmap(userId: String, avatarResult: AvatarResult) {
    val imageBytes =
        db.collection("users").document(userId).get().addOnSuccessListener { fields ->
            val imageBytes = Base64.decode(fields.get("image").toString(), Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            avatarResult.onResult(decodedImage)
        }

}

//Получение расписаний где юзер админ
fun getAdminSchedules(user: User, scheduleResult: ScheduleResult) {
    db.collection("schedules").whereEqualTo("admin_id", user.Id).get().addOnSuccessListener {
        val documents = it.documents
        var schedulesModel = mutableListOf<Schedule>()

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
                    DefaultScheduleOption()
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
    }
}

// Получение вссех юзеров расписания
fun getScheduleUsers(schedule: Schedule, usersResult: UsersResult) {
    db.collection("schedules").document(schedule.ID).get().addOnSuccessListener { fields ->
        var usersModel = mutableListOf<User>()

        (fields.get("users_ids") as List<String>).forEach { id ->
            getUserFromId(id, object : UserResult {
                override fun onResult(user: User) {
                    usersModel.add(user)
                }

                override fun onError(error: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        usersResult.onResult(usersModel)
    }
}

// Получение юзера по его ID
fun getUserFromId(userId: String, userResult: UserResult) {
    db.collection("schedules").whereEqualTo("id", userId).get().addOnSuccessListener {
        val documents = it.documents

        documents.forEach { doc ->
            getUserBitmap(doc.get("id").toString(), object : AvatarResult {
                override fun onResult(bitmap: Bitmap) {
                    val userModel = User(
                        doc.get("name").toString(),
                        doc.get("surname").toString(),
                        bitmap,
                        doc.get("id").toString()
                    )
                    userResult.onResult(user = userModel)
                }
            })
        }
    }.addOnFailureListener {
        userResult.onError(it)
    }
}

//Получение расписаний где юзер простой смертный
fun getUserSchedules(user: User, scheduleResult: ScheduleResult) {
    db.collection("schedules").whereArrayContains("users_ids", user.Id).get().addOnSuccessListener {
        val documents = it.documents
        var schedulesModel = mutableListOf<Schedule>()

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
                    DefaultScheduleOption()
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
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

//Получение всех расписаний
fun getAllSchedules(scheduleResult: ScheduleResult) {

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
                    DefaultScheduleOption()
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

interface EnterInScheduleResult {
    fun onResult(isEntered: Boolean)
}


// Интерфейс когда нужно вернуть массив юзеров
interface UsersResult {
    fun onResult(users: List<User>)
    fun onError(error: Throwable)
}

interface UserResult {
    fun onResult(user: User)
    fun onError(error: Throwable)
}

interface AvatarResult {
    fun onResult(bitmap: Bitmap)
}

