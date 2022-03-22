package com.otavi.pl.backend.controler

import com.otavi.pl.backend.Ts3
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/test2")
    fun test(): String{
        val context2 = SecurityContextHolder.getContext()
        val authentication2 = context2.authentication
        val username2 = authentication2.name
        val principal = authentication2.principal
        val authorities = authentication2.authorities
        Ts3().sendTokenToUser("USUSDds",2)
        return username2
    }
}