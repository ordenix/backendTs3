package com.otavi.pl.backend.dataClass

data class UserStatusRule(
    var connect : Boolean = false,
    var time: Boolean = false,
    var ban: Boolean = false,
    var rules: Boolean = false,
    var status_register: Boolean = false
)
