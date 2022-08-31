package com.kikoproject.uwidget.localdb

import androidx.room.*
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.models.User

/**
 * DAO слой настроек приложения
 * @author Kiko
 */
@Dao
interface AppOptionsDao {
    @Query("SELECT * FROM options")
    fun get(): GeneralOptions?

    @Insert
    fun insertOption(vararg option: GeneralOptions)

    @Update
    fun updateOption(vararg option: GeneralOptions)
}