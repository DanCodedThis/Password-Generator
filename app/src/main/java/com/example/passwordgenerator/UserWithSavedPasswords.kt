package com.example.passwordgenerator

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithSavedPasswords(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val savedPasswords: List<SavedPassword>
)
