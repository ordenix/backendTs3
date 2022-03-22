package com.otavi.pl.backend.dataClass

data class RegisterUser(
    val login: String,
    val password: String,
    val uid: String,
    val dbid: Int,
    val token: String,
)
