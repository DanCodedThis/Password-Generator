package com.example.passwordgenerator

enum class DialogType(val title: String) {
    NONE(""),
    REGISTER("Register"),
    LOGIN("Login"),
    INFO("Information"),
    CHANGE("Change"),
    DELETE("Confirm delete"),
    UNREGISTER("Confirm unregister"),
    LOGOUT("Confirm logout")
}