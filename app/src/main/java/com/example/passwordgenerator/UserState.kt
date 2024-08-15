package com.example.passwordgenerator

data class UserState(
    val savedPasswords: List<SavedPassword> = emptyList(),
    val name: String = "",
    val password: String = "",
    val chosenSavedPassword: SavedPassword? = null,
    val userId: String = "",
    val generatedPassword: String = "",
    val dialogType: DialogType = DialogType.NONE,
    val passwordVisibility: Boolean = true
)
