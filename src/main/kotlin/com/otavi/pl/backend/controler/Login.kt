package com.otavi.pl.backend.controler

import com.otavi.pl.backend.config.JwtFilter
import com.otavi.pl.backend.config.JwtUtil
import com.otavi.pl.backend.dao.UserForm
import com.otavi.pl.backend.dao.UserJwt
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Login {
    @Value("\${spring.jwt.secret}")
    private val secret: String? = null
    @GetMapping("/account_login")
    fun login(): String{
        val test= UserJwt(DBID = 1, UID = "sss", role = "ssss")
        return JwtUtil().generateToken(test, secret)
    }
}