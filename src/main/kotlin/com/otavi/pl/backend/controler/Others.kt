package com.otavi.pl.backend.controler

import com.otavi.pl.backend.dataClass.ClientHost
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/public")
class Others {

    @GetMapping("/get_ip")
    fun getIp(request: HttpServletRequest):ClientHost {
        val requestHeader = request.getHeader("X-Forwarded-For")
        return if (requestHeader != null){
            (ClientHost(client_Host = requestHeader))
        } else ClientHost(client_Host = request.remoteAddr)

    }

}