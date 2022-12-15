package com.kikoproject.uwidget.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kikoproject.uwidget.models.GeneralOptions

/**
 * DAO слой настроек приложения
 * @author Kiko
 */
@Dao
interface AppOptionsDao {
    @Query("SELECT * FROM options")
    fun get(): GeneralOptions

    @Insert
    fun insertOption(vararg option: GeneralOptions)

    @Update
    fun updateOption(vararg option: GeneralOptions)
}