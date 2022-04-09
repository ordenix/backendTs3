package com.otavi.pl.backend.dataClass

data class PutBan(
    val ban_client_dbid: Int,
    val ban_id: Int,
    val action_id: Int,
    val additional_info: String,
    val time: Int
)
