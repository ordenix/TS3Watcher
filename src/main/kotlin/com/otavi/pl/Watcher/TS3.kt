package com.otavi.pl.Watcher

import com.github.theholywaffle.teamspeak3.TS3Api
import com.github.theholywaffle.teamspeak3.TS3Config
import com.github.theholywaffle.teamspeak3.TS3Query

class TS3(private val config: TS3Config = TS3Config(),
          private val settings: TS3Settings = TS3Settings()) {
    private var query: TS3Query
    private var api: TS3Api
    init {
        config.setHost(settings.host)
        config.setQueryPort(settings.port)
        config.setFloodRate(TS3Query.FloodRate.UNLIMITED)
        query = TS3Query(config)
        query.connect()
        api = query.api
        api.login(settings.login, settings.password)
        api.selectVirtualServerById(settings.virtualServer)
        try {
            api.setNickname(settings.nickName)
        } catch (e: com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException) {
            println("Nick used")
        }

    }
}