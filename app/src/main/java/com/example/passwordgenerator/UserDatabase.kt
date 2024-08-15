package com.example.passwordgenerator

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [User::class, SavedPassword::class],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {

    abstract val dao: UserDao

}