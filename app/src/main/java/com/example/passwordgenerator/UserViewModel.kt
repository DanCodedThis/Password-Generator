package com.example.passwordgenerator

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class UserViewModel(
    private val dao: UserDao
): ViewModel() {
    private val symbols: MutableList<Char> = mutableListOf<Char>().apply {
        addAll(('a'..'z'))
        addAll(('0'..'9'))
        addAll(('A'..'Z'))
        addAll("!@#$%^&*()_-[]{}|:;,.<>?".toList())
    }
    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.CopyPassword -> {
                copyPassword(event.password, event.clipboardManager, event.context)
            }
            is UserEvent.DeleteSavedPassword -> {
                viewModelScope.launch {
                    dao.deletePassword(event.savedPassword)
                    val userId = state.value.userId
                    if (userId != "") {
                        _state.update { it.copy(
                            dialogType = DialogType.NONE,
                            savedPasswords = dao.getUserWithSavedPasswords(state.value.userId).savedPasswords
                        ) }
                    }
                }
                Toast.makeText(event.context, "Successfully deleted this saved password!", Toast.LENGTH_SHORT).show()
            }
            is UserEvent.GeneratePassword -> {
                val generatedPassword = generatePassword()
                val userId = state.value.userId
                if (userId != "") {
                    viewModelScope.launch {
                        dao.upsertPassword(
                            SavedPassword(
                                userId,
                                "",
                                "",
                                generatedPassword
                            )
                        )
                        _state.update { it.copy(
                            savedPasswords = dao.getUserWithSavedPasswords(state.value.userId).savedPasswords
                        ) }
                    }
                }
                copyPassword(generatedPassword, event.clipboardManager, event.context)
                _state.update { it.copy(
                    generatedPassword = generatedPassword
                ) }
            }
            is UserEvent.Login -> {
                login(event.context)
            }
            is UserEvent.Logout -> {
                logout(event.context)
            }
            is UserEvent.Register -> {
                val name = state.value.name
                val password = state.value.password
                if (name.isBlank()
                    || password.isBlank()) {
                    return
                }
                viewModelScope.launch {
                    try {
                        dao.register(User(name, password, UUID.nameUUIDFromBytes(name.toByteArray()).toString()))
                        login(event.context, true)
                        Toast.makeText(event.context, "Successfully registered and logged in to this account!", Toast.LENGTH_SHORT).show()
                    } catch (_: Exception) {
                        hideDialog()
                        Toast.makeText(event.context, "This account already exists!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is UserEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is UserEvent.SetPassword -> {
                _state.update { it.copy(
                    password = event.password
                ) }
            }
            is UserEvent.SetChosenSavedNote -> {
                _state.update { it.copy(
                    chosenSavedPassword = it.chosenSavedPassword?.copy(
                        note = event.note
                    )
                ) }
            }
            is UserEvent.SetChosenSavedPassword -> {
                _state.update { it.copy(
                    chosenSavedPassword = it.chosenSavedPassword?.copy(
                        password = event.password
                    )
                ) }
            }
            is UserEvent.Unregister -> {
                viewModelScope.launch {
                    state.value.savedPasswords.forEach {
                        dao.deletePassword(it)
                    }
                }
                viewModelScope.launch {
                    dao.unregister(dao.getUser(state.value.userId))
                }
                logout(event.context, true)
                Toast.makeText(event.context, "Successfully unregistered and logged off from this account!", Toast.LENGTH_SHORT).show()
            }
            is UserEvent.ChangeSavedPassword -> {
                viewModelScope.launch {
                    val chosenSavedPassword = state.value.chosenSavedPassword
                    if (chosenSavedPassword != null) {
                        dao.upsertPassword(event.savedPassword.copy(
                            title = event.savedPassword.title,
                            note = chosenSavedPassword.note,
                            password = chosenSavedPassword.password
                        ))
                        _state.update { it.copy(
                            savedPasswords = dao.getUserWithSavedPasswords(state.value.userId).savedPasswords,
                            dialogType = DialogType.NONE
                        ) }
                        Toast.makeText(event.context, "Successfully changed this saved password!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            UserEvent.HideDialog -> {
                hideDialog()
            }
            is UserEvent.ShowDialog -> {
                _state.update { it.copy(
                    dialogType = event.dialogType,
                    chosenSavedPassword = event.chosenSavedPassword,
                    passwordVisibility = true
                ) }
            }
            is UserEvent.SetChosenSavedTitle -> {
                _state.update { it.copy(
                    chosenSavedPassword = it.chosenSavedPassword?.copy(
                        title = event.title
                    )
                ) }
            }
            UserEvent.ChangeVisibility -> {
                _state.update { it.copy(
                    passwordVisibility = !state.value.passwordVisibility
                ) }
            }
        }
    }
    private fun generatePassword(): String {
        return buildString {
            repeat(16) {
                append(symbols[Random.nextInt(0, symbols.size)])
            }
        }
    }
    private fun login(context: Context, silent: Boolean = false) {
        val name = state.value.name
        val password = state.value.password
        if (name.isBlank()
            || password.isBlank()) {
            return
        }
        viewModelScope.launch {
            try {
                val userWithSavedPasswords = dao.getUserWithSavedPasswords(dao.login(name, password).id)
                _state.update { it.copy(
                    name = "",
                    password = "",
                    userId = userWithSavedPasswords.user.id,
                    dialogType = DialogType.NONE,
                    generatedPassword = "",
                    savedPasswords = userWithSavedPasswords.savedPasswords
                ) }
                if (!silent) {
                    Toast.makeText(context, "Logged In!", Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                hideDialog()
                Toast.makeText(context, "Wrong name and/or password!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun logout(context: Context, silent: Boolean = false) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    savedPasswords = emptyList(),
                    userId = "",
                    dialogType = DialogType.NONE,
                    generatedPassword = ""
                )
            }
            if (!silent) {
                Toast.makeText(context, "Logged Out!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun copyPassword(password: String, clipboardManager: ClipboardManager, context: Context) {
        clipboardManager.setText(AnnotatedString(password))
        Toast.makeText(context, "Copied this password to clipboard!", Toast.LENGTH_SHORT).show()
    }
    private fun hideDialog() {
        _state.update { it.copy(
            chosenSavedPassword = null,
            name = "",
            password = "",
            dialogType = DialogType.NONE,
            passwordVisibility = true
        ) }
    }
}