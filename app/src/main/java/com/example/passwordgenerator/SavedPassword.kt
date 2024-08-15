package com.example.passwordgenerator

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedPassword(
    val userId: String,
    val title: String,
    val note: String,
    val password: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
