package com.example.passwordgenerator

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name: String,
    val password: String,
    @PrimaryKey(autoGenerate = false)
    val id: String
)
