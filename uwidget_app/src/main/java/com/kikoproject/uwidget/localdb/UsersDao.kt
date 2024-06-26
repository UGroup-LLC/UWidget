package com.kikoproject.uwidget.localdb

import androidx.room.*
import com.kikoproject.uwidget.models.User

/**
 * DAO слой пользователей
 * @author Kiko
 */
@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun get(): User

    @Query("SELECT * FROM users WHERE id LIKE :userId")
    fun findById(userId: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun updateTodo(vararg user: User)
}