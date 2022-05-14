package com.otavi.pl.backend.dataClass

data class BanParametersToAdd(
    var time_from: Int = 0,
    val time_from_setting: String = "",
    var time_to: Int  = 0,
    var time_to_setting: String = "",
    var times_this: Int = 0,
    var times_all: Int = 0,
    var action_name: String = "",
    var action_id: Int = 0,
    var timing: Boolean = false,
    var ignore_ban: Boolean = false,
)
