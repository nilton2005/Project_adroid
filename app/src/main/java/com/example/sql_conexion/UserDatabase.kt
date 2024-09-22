package com.example.sql_conexion

import androidx.room.Database
import androidx.room.RoomDatabase

// Definir la base de datos Room
@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}