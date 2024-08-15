package com.example.passwordgenerator

import android.content.Context
import androidx.compose.ui.platform.ClipboardManager

sealed interface UserEvent {
    data class Register(val context: Context): UserEvent
    data class Unregister(val context: Context): UserEvent

    data class Login(val context: Context): UserEvent
    data class SetName(val name: String) : UserEvent
    data class SetPassword(val password: String) : UserEvent
    data class Logout(val context: Context): UserEvent

    data class GeneratePassword(val clipboardManager: ClipboardManager, val context: Context): UserEvent
    data class CopyPassword(val password: String, val clipboardManager: ClipboardManager, val context: Context): UserEvent

    data class SetChosenSavedPassword(val password: String): UserEvent
    data class SetChosenSavedNote(val note: String): UserEvent
    data class SetChosenSavedTitle(val title: String): UserEvent
    data class ChangeSavedPassword(val savedPassword: SavedPassword, val context: Context): UserEvent
    data class DeleteSavedPassword(val savedPassword: SavedPassword, val context: Context): UserEvent
    data object ChangeVisibility: UserEvent

    data class ShowDialog(val dialogType: DialogType, val chosenSavedPassword: SavedPassword? = null): UserEvent
    data object HideDialog: UserEvent
}