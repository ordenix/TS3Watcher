package com.otavi.pl.Watcher

import com.github.theholywaffle.teamspeak3.TS3Api
import com.github.theholywaffle.teamspeak3.TS3Config
import com.github.theholywaffle.teamspeak3.TS3Query
import com.github.theholywaffle.teamspeak3.api.ChannelProperty
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel
import com.otavi.pl.Watcher.config.TS3Settings

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

    fun getCurrentChannelList(): MutableList<Channel>? {
        val channelList: MutableList<Channel>? = api.channels
        query.exit()
        return channelList
    }

    fun createChannelUnderParent(parentId: Int, name: String) {
        val options = mutableMapOf<ChannelProperty, String>()
        options[ChannelProperty.CPID] = parentId.toString()
        options[ChannelProperty.CHANNEL_FLAG_PERMANENT] = "1"
        options[ChannelProperty.CHANNEL_CODEC] = "4"
        options[ChannelProperty.CHANNEL_CODEC_QUALITY] = "6"
        options[ChannelProperty.CHANNEL_MAXCLIENTS] = "0"
        options[ChannelProperty.CHANNEL_MAXFAMILYCLIENTS] = "0"
        api.createChannel(name, options)
        query.exit()
    }

    fun setTopic(channelId: Int, name: String) {
        val options = mutableMapOf<ChannelProperty, String>()
        options[ChannelProperty.CHANNEL_TOPIC] = name
        api.editChannel(channelId, options)
        query.exit()
    }
}