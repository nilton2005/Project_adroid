package com.example.sql_conexion

import androidx.room.RoomDatabase

abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}