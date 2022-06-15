package com.kikoproject.uwidget.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule

/**
 * Главная локальная БД
 * @author Kiko
 */
@Database(entities = [User::class, Schedule::class], version = 1)
@TypeConverters(MainConverter::class)
abstract class MainDataBase : RoomDatabase() {
    abstract fun userDao(): UsersDao
    abstract fun scheduleDao(): SchedulesDao
}