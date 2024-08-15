package com.example.passwordgenerator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun GenericDialog(
    state: UserState,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    when (state.dialogType) {
        DialogType.NONE -> {}
        DialogType.REGISTER -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = { onEvent(UserEvent.Register(context)) }) {
                        Text(text = "Register")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Back")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
                text = {
                       Column(
                           verticalArrangement = Arrangement.spacedBy(8.dp)
                       ) {
                           TextField(
                               value = state.name,
                               onValueChange = { onEvent(UserEvent.SetName(it)) },
                               placeholder = {
                                   Text("Name")
                               }
                           )
                           TextField(
                               value = state.password,
                               onValueChange = { onEvent(UserEvent.SetPassword(it)) },
                               placeholder = {
                                   Text("Password")
                               },
                               visualTransformation = PasswordVisualTransformation(),
                               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                           )
                       }
                },
            )
        }
        DialogType.LOGIN -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = { onEvent(UserEvent.Login(context)) }) {
                        Text(text = "Login")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Back")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = state.name,
                            onValueChange = { onEvent(UserEvent.SetName(it)) },
                            placeholder = {
                                Text("Name")
                            }
                        )
                        TextField(
                            value = state.password,
                            onValueChange = { onEvent(UserEvent.SetPassword(it)) },
                            placeholder = {
                                Text("Password")
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }
                },
            )
        }
        DialogType.CHANGE -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = {
                        onEvent(UserEvent.ChangeSavedPassword(state.chosenSavedPassword!!, context))
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Deny")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = state.chosenSavedPassword!!.title,
                            onValueChange = { onEvent(UserEvent.SetChosenSavedTitle(it)) },
                            placeholder = {
                                Text("Title")
                            }
                        )
                        TextField(
                            value = state.chosenSavedPassword.note,
                            onValueChange = { onEvent(UserEvent.SetChosenSavedNote(it)) },
                            placeholder = {
                                Text("Note")
                            }
                        )
                        Row {
                            TextField(
                                value = state.chosenSavedPassword.password,
                                onValueChange = { onEvent(UserEvent.SetChosenSavedPassword(it)) },
                                placeholder = {
                                    Text("Password")
                                },
                                visualTransformation = if (state.passwordVisibility) PasswordVisualTransformation()
                                else VisualTransformation.None,
                                keyboardOptions = KeyboardOptions(keyboardType = if (state.passwordVisibility) KeyboardType.Password
                                else KeyboardType.Text),
                                modifier = Modifier.weight(4f, true)
                            )
                            IconButton(onClick = { onEvent(UserEvent.ChangeVisibility)
                            },
                                modifier = Modifier.weight(1f, true)
                            ) {
                                Icon(
                                    painter = painterResource(id = if (state.passwordVisibility) R.drawable.outline_visibility_off_24
                                    else R.drawable.outline_visibility_24),
                                    contentDescription = "Delete saved password"
                                )
                            }
                        }
                    }
                }
            )
        }
        DialogType.DELETE -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = {
                        onEvent(UserEvent.DeleteSavedPassword(state.chosenSavedPassword!!, context))
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Deny")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
                text = {
                    Text(text = "Delete this password")
                }
            )
        }
        DialogType.UNREGISTER -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = {
                        onEvent(UserEvent.Unregister(context))
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Deny")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
                text = {
                    Text(text = "Unregister this account (deletes all saved passwords!)")
                }
            )
        }
        DialogType.LOGOUT -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = {
                        onEvent(UserEvent.Logout(context))
                    }) {
                        Text(text = "Logout")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Back")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
            )
        }
        DialogType.INFO -> {
            AlertDialog(
                modifier = modifier,
                onDismissRequest = { onEvent(UserEvent.HideDialog) },
                confirmButton = {
                    Button(onClick = {
                        onEvent(UserEvent.ShowDialog(DialogType.CHANGE, state.chosenSavedPassword))
                    }) {
                        Text(text = "Change")
                    }
                },
                dismissButton = {
                    Button(onClick = { onEvent(UserEvent.HideDialog) }) {
                        Text(text = "Back")
                    }
                },
                title = {
                    Text(text = state.dialogType.title)
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = state.chosenSavedPassword!!.title,
                            onValueChange = { },
                            placeholder = {
                                Text("Title")
                            },
                            readOnly = true
                        )
                        TextField(
                            value = state.chosenSavedPassword.note,
                            onValueChange = { },
                            placeholder = {
                                Text("Note")
                            },
                            readOnly = true
                        )
                        Row {
                            TextField(
                                value = state.chosenSavedPassword.password,
                                onValueChange = { },
                                placeholder = {
                                    Text("Password")
                                },
                                readOnly = true,
                                visualTransformation = if (state.passwordVisibility) PasswordVisualTransformation()
                                else VisualTransformation.None,
                                keyboardOptions = KeyboardOptions(keyboardType = if (state.passwordVisibility) KeyboardType.Password
                                else KeyboardType.Text),
                                modifier = Modifier.weight(4f, true)
                            )
                            IconButton(onClick = { onEvent(UserEvent.ChangeVisibility)
                            },
                                modifier = Modifier.weight(1f, true)
                            ) {
                                Icon(
                                    painter = painterResource(id = if (state.passwordVisibility) R.drawable.outline_visibility_off_24
                                    else R.drawable.outline_visibility_24),
                                    contentDescription = "Delete saved password"
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}