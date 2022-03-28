package com.otavi.pl.backend.dataClass

data class LastServerStats(
    val ping: Double = 0.0,
    val current_user: Int = 0,
    val pack_loss: Double = 0.0
)
