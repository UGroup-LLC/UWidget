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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.dialogs.ShowLoadingDialog
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.MainColors
import com.kikoproject.uwidget.ui.theme.monetEngineIsEnabled
import com.kikoproject.uwidget.ui.theme.themeAppMode
import kotlin.random.Random

/**
 * Проверка пользователя в БД
 * Если пользователь не авторизирован отправляет на GoogleAuth
 * Если пользователь авторизирован но не существует в БД отправляет в RegistrationInfo
 * Если все есть отправляет в Dashboard
 *
 * @param state тображение диалога загрузки
 * @param account Google аккаунт
 *
 * @author Kiko
 */
@Composable
fun CheckUserInDB(
    context: Context,
    state: MutableState<Boolean>,
    account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context),
) {
    navController.popBackStack()

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
                        roomDb!!.userDao().insertUser(user)
                        state.value = false

                        // Проверит есть ли аккаунт в локальной БД и занесет если нет
                        insertAccountInRoom(curUser)

                        updateAllData(
                            user = curUser,
                            content = { myScheduleUser, myScheduleAdmin ->

                                //********** Записываем в локальную БД **********

                                myScheduleUser.forEach { schedule ->
                                    roomDb!!.scheduleDao().insertAll(schedule)
                                }
                                myScheduleAdmin.forEach { schedule ->
                                    roomDb!!.scheduleDao().insertAll(schedule)
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

/**
 * Сохраняет пользователя в локальную БД
 *
 * @author Kiko
 */
fun insertAccountInRoom(user: User?) {
    if (user != null) {
        if (roomDb!!.userDao().findById(user.Id) == null) {
            roomDb!!.userDao().insertUser()
        }
    }
}

/**
 * Сохраняет расписание в локальную БД
 *
 * @author Kiko
 */
fun createScheduleInRoomDB(schedule: Schedule) {
    roomDb!!.scheduleDao().insertAll(schedule)
}

/**
 * Получает рандомное расписание пользователя
 * Сначала пытается получить расписание где пользователь администратор
 * А затем где обычный пользователь, если же не находит такие, то возвращает null
 *
 * @param mySchedulesAdmin лист расписаний где пользователь администратор
 * @param mySchedulesUser лист расписаний где пользователь обычный юзер
 */
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

/**
 * Генерирует код для расписания который еще не существует в БД
 *
 * @param generatedCodeResult результат генирации
 *
 * @author Kiko
 */
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

/**
 * Метод содержащий в себе прогрузку всех юзеров расписания
 * Отображает загрзку пока не получит лист юзеров
 *
 * @param content будущее использование листа юзеров
 * @param schedule расписание
 *
 * @author Kiko
 */
@Composable
fun MembersOnlineContent(
    content: @Composable (users: List<User>) -> Unit,
    schedule: Schedule,
) {
    val showContent = remember { mutableStateOf(false) }
    val scheduleUsers = remember {
        mutableStateOf(listOf<User>())
    }

    if (showContent.value) {
        content(scheduleUsers.value)
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    // Если убрать то будет постоянно обновляться (в будущем доработать чтобы адекватно работало TODO(Сделать))
    if (!showContent.value) {
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

/**
 * Метод содержащий в себе прогрузку всех расписаний пользователя где он является
 * администратором или же обычный юзером
 *
 * @param content будущее использование расписаний
 * @param user пользователь
 *
 * @author Kiko
 */
@Composable
fun OnlineContent(
    content: @Composable (mySchedulesUser: List<Schedule>, mySchedulesAdmin: List<Schedule>) -> Unit,
    user: User,
) {
    val showContent = remember { mutableStateOf(false) }
    val mySchedulesUser = remember {
        mutableStateOf(listOf<Schedule>())
    }
    val mySchedulesAdmin = remember {
        mutableStateOf(listOf<Schedule>())
    }

    if (showContent.value) {
        val visible = remember { mutableStateOf(true) }
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            content(mySchedulesUser.value, mySchedulesAdmin.value)
        }
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    }

    getAdminSchedules(
        user = user,
        scheduleResult = object : ScheduleResult {
            override fun onResult(adminSchedule: List<Schedule>) {
                getUserSchedules(
                    user = user,
                    scheduleResult = object : ScheduleResult {
                        override fun onResult(userSchedule: List<Schedule>) {
                            mySchedulesUser.value = userSchedule
                            mySchedulesAdmin.value = adminSchedule
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


/**
 * Делает то же самое что и OnlineContent
 * но не является Composable не показывает загрузку контента графически
 *
 * @param content будущее использование листа юзеров
 * @param user пользователь
 *
 * @author Kiko
 */
fun updateAllData(
    content: (mySchedulesUser: List<Schedule>, mySchedulesAdmin: List<Schedule>) -> Unit,
    user: User,
) {
    getAdminSchedules(
        user = user,
        scheduleResult = object : ScheduleResult {
            override fun onResult(schedulesAdmin: List<Schedule>) {
                getUserSchedules(
                    user = user,
                    scheduleResult = object : ScheduleResult {
                        override fun onResult(schedulesUser: List<Schedule>) {
                            content(schedulesUser, schedulesAdmin)
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

/**
 * Удаляет пользователя по ID из указанного расписания
 *
 * @param schedule расписание
 * @param userId ID пользователя для удаления
 *
 * @author Kiko
 */
fun outFromSchedule(schedule: Schedule, userId: String) {
    val newUsersId = schedule.UsersID.toMutableList()
    newUsersId.remove(userId)
    roomDb!!.scheduleDao().update(schedule.copy(UsersID = newUsersId))

    db.collection("schedules").document(schedule.ID).update(mapOf(Pair("users_ids", newUsersId)))

    curSchedule = schedule.copy(UsersID = newUsersId)
}

/**
 * Полное удаление расписание из всех БД
 *
 * @param schedule расписание
 *
 * @author Kiko
 */
fun deleteSchedule(schedule: Schedule) {
    roomDb!!.scheduleDao().delete(schedule = schedule)
    db.collection("schedules").document(schedule.ID).delete()
}

/**
 * Создает расписание в онлайн БД
 *
 * @param schedule расписание
 *
 * @author Kiko
 */
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

/**
 * Вход в расписание по коду приглашения
 *
 * @param code код входа в расписание
 * @param enterInScheduleResult результат входа в расписание
 *
 * @author Kiko
 */
fun enterInSchedule(code: String, enterInScheduleResult: EnterInScheduleResult) {
    db.collection("schedules")
        .whereEqualTo("code", code)
        .get().addOnSuccessListener {
            if (it.documents.size != 0) {
                val doc = it.documents[0]
                val schedule = Schedule(
                    doc.id,
                    doc.get("name").toString(),
                    doc.get("admin_id").toString(),
                    doc.get("users_ids") as List<String>,
                    doc.get("schedule") as Map<String, MutableList<String>>,
                    doc.get("code").toString() as String?,
                    doc.get("time") as List<String>,
                    doc.get("category").toString()
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
            } else {
                enterInScheduleResult.onResult(false)
            }
        }.addOnFailureListener {
            enterInScheduleResult.onResult(false)
        }
}

/**
 * Получает аватар пользователя по его ID
 *
 * @param userId ID пользователя
 * @param avatarResult результат получения автара
 *
 * @author Kiko
 */
fun getUserBitmap(userId: String, avatarResult: AvatarResult) {
    db.collection("users").document(userId).get().addOnSuccessListener { fields ->
        val imageBytes = Base64.decode(fields.get("image").toString(), Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatarResult.onResult(decodedImage)
    }
}

/**
 * Получение расписаний где указанный пользователь администратор
 *
 * @param user пользователь
 * @param scheduleResult результаты поиска расписаний
 *
 * @author Kiko
 */
fun getAdminSchedules(user: User, scheduleResult: ScheduleResult) {
    db.collection("schedules").whereEqualTo("admin_id", user.Id).get().addOnSuccessListener {
        val documents = it.documents
        val schedulesModel = mutableListOf<Schedule>()

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
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
    }
}

/**
 * Получение всех пользователей в указанном расписании
 *
 * @param schedule расписание
 * @param usersResult результат получения
 *
 * @author Kiko
 */
fun getScheduleUsers(schedule: Schedule, usersResult: UsersResult) {
    db.collection("schedules").document(schedule.ID).get().addOnSuccessListener { fields ->
        val usersModel = mutableListOf<User>()

        val field = (fields.get("users_ids") as List<String>)
        field.forEachIndexed { _, id ->
            if (field.size == 1 && field[0] == "") {
                usersResult.onResult(usersModel)
            }
            getUserFromId(id, object : UserResult {
                override fun onResult(user: User?) {
                    if (user != null) {
                        usersModel.add(user)
                    }
                    if (id == field.last()) {
                        usersResult.onResult(usersModel)
                    }
                }

                override fun onError(error: Throwable) {

                }
            })
        }
    }
}

/**
 * Возвращает User по ID пользователя
 *
 * @param userId ID пользователя
 * @param userResult результат получения
 * @param contentIfError если произошла ошибка и пользователя нет, то срабатывает этот параметр
 *
 * @author Kiko
 */
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
        } catch (exception: Exception) {
            userResult.onError(exception)
        }
    }.addOnFailureListener {
        userResult.onError(it)
    }
}

/**
 * Получение расписаний где указанный пользователь простой юзер
 *
 * @param user пользователь
 * @param scheduleResult результаты поиска расписаний
 *
 * @author Kiko
 */
fun getUserSchedules(user: User, scheduleResult: ScheduleResult) {
    db.collection("schedules").whereArrayContains("users_ids", user.Id).get().addOnSuccessListener {
        val documents = it.documents
        val schedulesModel = mutableListOf<Schedule>()

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
                )
            )
        }

        scheduleResult.onResult(schedulesModel)

    }.addOnFailureListener {
        scheduleResult.onError(it)
    }
}

fun changeThemeColor(colorType: MainColors, colorValue: Color) {
    val genOptions = roomDb!!.optionsDao().get()
    val newColors = mutableListOf<Color>()
    newColors.addAll(genOptions.Colors) // Берем все цвета
    newColors[colorType.value] = colorValue // Изменяем нужный нам цвет
    roomDb!!
        .optionsDao()
        .updateOption(
            genOptions.copy(
                Colors = newColors // Возращаем в базу
            )
        )

}

fun changeTheme(themeIs: Boolean) {
    val newGenOpt = roomDb!!.optionsDao().get().copy(Theme = themeIs)
    roomDb!!.optionsDao().updateOption(newGenOpt)
    themeAppMode.value = themeIs
}

fun changeMonetEngine(isEnable: Boolean) {
    val newGenOpt = roomDb!!.optionsDao().get().copy(IsMonetEngineEnable = isEnable)
    roomDb!!.optionsDao().updateOption(newGenOpt)
    monetEngineIsEnabled.value = isEnable
}

fun addOldThemeColor(colorValue: Color) {
    val genOptions = roomDb!!.optionsDao().get()

    val newColorList = mutableListOf<Color>()
    if (genOptions.OldColors != null) {
        newColorList.addAll(genOptions.OldColors!!)
    }
    newColorList.add(colorValue)
    val newGenOpt = genOptions.copy(OldColors = newColorList)
    roomDb!!.optionsDao().updateOption(newGenOpt)
    roomDb!!.optionsDao().get()
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

