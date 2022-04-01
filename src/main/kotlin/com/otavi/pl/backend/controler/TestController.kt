package com.otavi.pl.backend.controler

import InfluxDb
import com.github.theholywaffle.teamspeak3.api.wrapper.Client
import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.config.NavElements
import com.otavi.pl.backend.dataClass.ClientOnlineOnTsByIp
import com.otavi.pl.backend.dataClass.NavElement
import com.otavi.pl.backend.repository.BanHistoryTableRepository
import com.otavi.pl.backend.repository.PrivilageToRankRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.ArrayList

@RestController
@RequestMapping("/test")
class TestController(val repo: BanHistoryTableRepository) {

    @GetMapping("/test2")
    fun test(): Any{
        val context2 = SecurityContextHolder.getContext()
        val authentication2 = context2.authentication
        val username2 = authentication2.name
        val principal = authentication2.principal
        val authorities = authentication2.authorities
        //Ts3().sendTokenToUser("USUSDds",2)
        val OnLineClients: MutableList<Client> = Ts3().listOnLineByIp("89.64.49.162")
        val tsestss = InfluxDb().getLastStats()
        val ssssssssssss = repo.findByBanClientDbid_DbidAndTimeAddIsGreaterThan(dbid = 106466, timeAdd = 1648816331)
        println(tsestss)
        var response: ArrayList<ClientOnlineOnTsByIp> = ArrayList()
        OnLineClients.forEach { client ->
            response.add(ClientOnlineOnTsByIp(DBID = client.databaseId, IP = client.ip, Nick = client.nickname, UID = client.uniqueIdentifier))

        }
        return response
    }
}