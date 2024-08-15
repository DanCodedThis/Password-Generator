package com.example.passwordgenerator

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: User): Long

    @Delete
    suspend fun unregister(user: User)

    @Transaction
    @Query("SELECT * FROM User WHERE name = :name and password = :password")
    suspend fun login(name: String, password: String): User

    @Transaction
    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUser(id: String): User

    @Transaction
    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserWithSavedPasswords(id: String): UserWithSavedPasswords

    @Upsert
    suspend fun upsertPassword(savedPassword: SavedPassword)

    @Delete
    suspend fun deletePassword(savedPassword: SavedPassword)
}