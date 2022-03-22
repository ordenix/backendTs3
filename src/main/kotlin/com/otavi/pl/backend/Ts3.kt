package com.otavi.pl.backend

import com.github.theholywaffle.teamspeak3.TS3Config
import com.github.theholywaffle.teamspeak3.TS3Query
import com.otavi.pl.backend.dataClass.TS3Settings


class Ts3 {

    fun sendTokenToUser(token: String, dbid: Int) {
        val config = TS3Config()
        val settings = TS3Settings()
        config.setHost(settings.host)
        config.setQueryPort(settings.port)
        config.setFloodRate(TS3Query.FloodRate.UNLIMITED)
        val query = TS3Query(config)
        query.connect()
        val api = query.api
        api.login(settings.login, settings.password)
        api.selectVirtualServerById(settings.virtualServer)
        try {
            api.setNickname(settings.nickName)
        } catch (e: com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException) {
          println("Nick used")
        } finally {
            val clientFromDb = api.getDatabaseClientInfo(dbid)
            val client = api.getClientByUId(clientFromDb["client_unique_identifier"])
            val version: String = client["client_version"]
            if (version.startsWith("5.0.0")) {
                api.sendPrivateMessage(client.id, ":wave: Witaj :arrow_right: ${client["client_nickname"]} :arrow_left:")
                api.sendPrivateMessage(client.id, "Twój klucz do autoryzowania się w systemie :customs: :  ${token}")
            } else {
                api.sendPrivateMessage(client.id, "[b]Witaj [COLOR=#ff0000] ${client["client_nickname"]} [/COLOR][/B]")
                api.sendPrivateMessage(client.id, "[b]Twój klucz do autoryzowania się w systemie to: [COLOR=#ff0000][U] ${token} [/COLOR][/B]")
            }
            query.exit()
        }
    }
}