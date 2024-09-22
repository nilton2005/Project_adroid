package com.example.sql_conexion

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>
    @Query("SELECT MAX(uid) FROM User")
    suspend fun getMaxId(): Long
    @Insert
    suspend fun insert(user: User)
    @Query("DELETE FROM User WHERE uid = :userId")
    suspend fun deleteByUserId(userId: Long)

}

