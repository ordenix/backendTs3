package com.otavi.pl.backend.controler

import com.otavi.pl.backend.dataClass.ClientHost
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/others")
class Others {

    @GetMapping("/get-ip")
    fun getIp(request: HttpServletRequest):ClientHost {
        val requestHeader = request.getHeader("X-Forwarded-For")
        return if (requestHeader != null){
            (ClientHost(client_host = requestHeader))
        } else ClientHost(client_host = request.remoteAddr)

    }

    @GetMapping("/last-stats")
    fun getLastStats() {

    }

}