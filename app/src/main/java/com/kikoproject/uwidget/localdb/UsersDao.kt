package com.kikoproject.uwidget.localdb

import androidx.room.*
import com.kikoproject.uwidget.models.User

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE id LIKE :userId")
    fun findById(userId: String): User?

    @Insert
    fun insertUser(vararg user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun updateTodo(vararg user: User)
}