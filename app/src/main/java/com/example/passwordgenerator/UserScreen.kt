package com.example.passwordgenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen (
    state: UserState,
    onEvent: (UserEvent) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Password Generator")
                },
                actions = {
                    if (state.userId != "") {
                        Button(onClick = {
                            onEvent(UserEvent.ShowDialog(DialogType.LOGOUT))
                        }) {
                            Text(text = "Logout")
                        }
                        Text(text = "/")
                        Button(onClick = {
                            onEvent(UserEvent.ShowDialog(DialogType.UNREGISTER))
                        }) {
                            Text(text = "Unregister")
                        }
                    } else {
                        Button(onClick = {
                            onEvent(UserEvent.ShowDialog(DialogType.LOGIN))
                        }) {
                            Text(text = "Login")
                        }
                        Text(text = "/")
                        Button(onClick = {
                            onEvent(UserEvent.ShowDialog(DialogType.REGISTER))
                        }) {
                            Text(text = "Register")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    TextField(
                        value = state.generatedPassword,
                        onValueChange = { },
                        placeholder = {
                            Text("Generated Password")
                        },
                        readOnly = true,
                        modifier = Modifier.weight(2f, true),
                        maxLines = 1
                    )
                    Button(onClick = {
                            onEvent(UserEvent.GeneratePassword(clipboardManager, context))
                        },
                        modifier = Modifier.weight(1f, true)
                        ) {
                            Text(
                                text = "Generate"
                            )
                    }
                }
            )
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        if (state.dialogType != DialogType.NONE) {
            GenericDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.savedPasswords) { savedPassword ->
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = savedPassword.title,
                        onValueChange = { },
                        placeholder = {
                            Text("Title")
                        },
                        enabled = false,
                        modifier = Modifier.weight(2f, true),
                        maxLines = 1
                    )
                    TextField(
                        value = savedPassword.password,
                        onValueChange = { },
                        placeholder = {
                            Text("Note")
                        },
                        enabled = false,
                        modifier = Modifier.weight(2f, true),
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    IconButton(onClick = {
                        onEvent(UserEvent.CopyPassword(savedPassword.password, clipboardManager, context))
                    },
                        modifier = Modifier.weight(1f, true)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = "Copy to clipboard"
                        )
                    }
                    IconButton(onClick = { onEvent(UserEvent.ShowDialog(DialogType.INFO, savedPassword))
                    },
                        modifier = Modifier.weight(1f, true)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_menu_book_24),
                            contentDescription = "Change saved password"
                        )
                    }
                    IconButton(onClick = { onEvent(UserEvent.ShowDialog(DialogType.DELETE, savedPassword))
                    },
                        modifier = Modifier.weight(1f, true)
                    ) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "Delete saved password"
                        )
                    }
                }
            }
        }
    }
}