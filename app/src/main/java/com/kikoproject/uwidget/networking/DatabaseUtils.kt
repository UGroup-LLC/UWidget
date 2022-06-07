package com.kikoproject.uwidget.networking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.DocumentSnapshot
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.DefaultScheduleOption
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav
import kotlin.random.Random

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
            val errorState = remember { mutableStateOf(false) }
            val errorMessage = remember { mutableStateOf("") }
            if (errorState.value) {
                ShowErrorDialog(text = errorMessage.value, false)
            }

            // **** Получение curUser ****
            getUserFromId(account.id.toString(), object : UserResult {
                override fun onResult(user: User?) {
                    if (user != null) {
                        curUser = user
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
                                // ******** Переход в DashBoard ********
                                navController.navigate(ScreenNav.Dashboard.route)
                            })
                    } else {
                        state.value = false
                        navController.navigate(ScreenNav.GoogleAuthNav.route) // Если нет аккаунта то отправляем на регистрацию/вход
                    }
                }

                override fun onError(error: Throwable) {
                    // Если пришли пиздарики
                    errorMessage.value = error.message.toString()
                    errorState.value = true
                }
            }, contentIfError = {
                state.value = false
                navController.navigate(ScreenNav.RegistrationNav.route)
            })
        }
    } else {
        navController.navigate(ScreenNav.GoogleAuthNav.route)
    }
}

// Сохраняет пользователя в локальной БД
fun insertAccountInRoom(user: User?) {
    if (user != null) {
        if (roomDb.userDao().findById(user.Id) == null) {
            roomDb.userDao().insertUser()
        }
    }
}

// Создает расписание в локальной БД
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

fun generateCode(generatedCodeResult: GeneratedCodeResult) {
    val code = Random.nextInt(100000, 999999).toString()
    db.collection("schedules")
        .whereEqualTo("code", code).get().addOnSuccessListener {
            if (it.documents.size == 0) {
                generatedCodeResult.onResult(code)
            } else {
                generateCode(object : GeneratedCodeResult {
                    override fun onResult(code: String) {
                        generatedCodeResult.onResult(code)
                    }

                    override fun onError(error: Throwable) {
                        generatedCodeResult.onError(error)
                    }

                })
            }
        }.addOnFailureListener {
            generatedCodeResult.onError(it)
        }
}

// Подгружает всех юзеров кто состоит в указанном расписании
@Composable
fun MembersOnlineContent(
    content: @Composable (users: List<User>) -> Unit,
    schedule: Schedule,
) {
    val showContent = remember { mutableStateOf(false) }
    var scheduleUsers = remember {
        mutableStateOf(listOf<User>())
    }

    if (showContent.value) {
        content(scheduleUsers.value)
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }

    // Если убрать то будет постоянно обновляться (в будущем доработать чтобы адекватно работало TODO(Сделать))
    if(!showContent.value) {
        getScheduleUsers(schedule = schedule, object : UsersResult {
            override fun onResult(users: List<User>) {
                scheduleUsers.value = users
                showContent.value = true
            }

            override fun onError(error: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}

// Прогружает расписания юзера пока не прогрузило, показывает loading ui
@Composable
fun OnlineContent(
    content: @Composable (mySchedulesUser: List<Schedule>, mySchedulesAdmin: List<Schedule>) -> Unit,
    user: User,
) {
    val showContent = remember { mutableStateOf(false) }
    var mySchedulesUser = remember {
        mutableStateOf(listOf<Schedule>())
    }
    var mySchedulesAdmin = remember {
        mutableStateOf(listOf<Schedule>())
    }

    if (showContent.value) {
        val visible = remember { mutableStateOf(true) }
        val density = LocalDensity.current
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            content(mySchedulesUser.value, mySchedulesAdmin.value)
        }
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }

    getAdminSchedules(
        user = user,
        scheduleResult = object : ScheduleResult {
            override fun onResult(mAdminSchedule: List<Schedule>) {
                getUserSchedules(
                    user = user,
                    scheduleResult = object : ScheduleResult {
                        override fun onResult(mUserSchedule: List<Schedule>) {
                            mySchedulesUser.value = mUserSchedule
                            mySchedulesAdmin.value = mAdminSchedule
                            showContent.value = true
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

// Создать расписание в бд
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

// Вход в расписание
fun enterInSchedule(code: String, enterInScheduleResult: EnterInScheduleResult) {
    db.collection("schedules")
        .whereEqualTo("code", code)
        .get().addOnSuccessListener {
            if (it.documents.size != 0) {
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
                if (!schedule.UsersID.contains(curUser.Id) && schedule.AdminID != curUser.Id
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
                } else {
                    enterInScheduleResult.onResult(false)
                }
            }
            else{
                enterInScheduleResult.onResult(false)
            }
        }.addOnFailureListener {
            enterInScheduleResult.onResult(false)
        }
}

// Получение картинки из юзера
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

        val field = (fields.get("users_ids") as List<String>)
        field.forEachIndexed { index, id ->
            getUserFromId(id, object : UserResult {
                override fun onResult(user: User?) {
                    if (user != null) {
                        usersModel.add(user)
                    }
                    if(id == field.last()){
                        usersResult.onResult(usersModel)
                    }
                }

                override fun onError(error: Throwable) {

                }

            })
        }
    }

}

// Получение юзера по его ID
fun getUserFromId(userId: String, userResult: UserResult, contentIfError: (() -> Unit)? = null) {
    db.collection("users").whereEqualTo("id", userId).get().addOnSuccessListener {
        try {
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

            if (documents.size == 0) {
                if (contentIfError == null) {
                    userResult.onError(Error("Внутренняя ошибка приложения!"))
                } else {
                    contentIfError()
                }
            }
        } catch (exep: Exception) {
            userResult.onError(exep)
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

interface GeneratedCodeResult {
    fun onResult(code: String)
    fun onError(error: Throwable)
}

interface UserResult {
    fun onResult(user: User?)
    fun onError(error: Throwable)
}

interface AvatarResult {
    fun onResult(bitmap: Bitmap)
}

