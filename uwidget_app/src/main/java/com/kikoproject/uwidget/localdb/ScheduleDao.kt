package com.kikoproject.uwidget.localdb

import androidx.room.*
import com.kikoproject.uwidget.models.schedules.Schedule

/**
 * DAO слой расписаний
 * @author Kiko
 */
@Dao
interface SchedulesDao {
    @Query("SELECT * FROM schedules")
    fun getAll(): List<Schedule>

    @Query("SELECT * FROM schedules WHERE ID LIKE :id")
    fun getWithId(id: String): Schedule

    @Query("SELECT SUBSTR(CAST(ABS(RANDOM()/1E+13) AS TEXT),1,6) AS random_num FROM schedules WHERE random_num NOT IN (SELECT JoinCode FROM schedules) LIMIT 1")
    fun getJoinCode(): String

    @Query("SELECT * FROM schedules WHERE adminId LIKE :mAdminId")
    fun getByUserId(mAdminId: String): List<Schedule>

    @Query("DELETE FROM schedules")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)


    @Update
    fun update(vararg schedule: Schedule)
}