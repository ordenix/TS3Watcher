package com.otavi.pl.Watcher

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel
import com.otavi.pl.Watcher.Entity.Channels
import com.otavi.pl.Watcher.repository.ChannelsRepository
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Configuration
@EnableScheduling
@EnableAsync
class Main(
    val channelsRepository: ChannelsRepository
) {
    private var logger = LoggerFactory.getLogger(javaClass.simpleName)
    @Scheduled(fixedRate = 1000 * 60 * 5)
    fun scheduleFixedRateTask() {

        //TS3().createChannelUnderParent(76837)
//        for (i in 1 .. 25) {
//            TS3().createChannelUnderParent(76837, name = "$i. Wolny [Otavi.pl]")
//        }
        logger.info("Run date checker")
        channelList(typeChannel = "normal", parentChannelId = 1222)
        dateChecker(1222)
        //channelList(typeChannel = "test", parentChannelId = 76837)
    }

    // TODO: helpers anomaly detector
    fun normalChannelOrderValidator(parentChannelId: Int) {
        val channelFromDb = channelsRepository.findByParentChannelId(parentChannelId)
        var sortNumber: Int = 1
        channelFromDb.forEach { element ->
            val channelNumber = element.channelName?.split(". ")?.get(0)?.toInt()

        }
    }

    fun dateChecker(parentChannelId: Int) {
        val channelFromDb = channelsRepository.findByParentChannelId(parentChannelId)
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dateDayFormatter = DateTimeFormatter.ofPattern("dd")
        val dateMonthFormatter = DateTimeFormatter.ofPattern("MM")
        val dateYearFormatter = DateTimeFormatter.ofPattern("yyyy")

        val dateDay = current.format(dateDayFormatter)
        val dateMonth = current.format(dateMonthFormatter)
        val dateYear = current.format(dateYearFormatter)
        var month: String = ""

        when (dateMonth) {
            "01" -> month = "Stycznia"
            "02" -> month = "Lutego"
            "03" -> month = "Marca"
            "04" -> month = "Kwietnia"
            "05" -> month = "Maja"
            "06" -> month = "Czerwca"
            "07" -> month = "Lipca"
            "08" -> month = "Sierpnia"
            "09" -> month = "Września"
            "10" -> month = "Października"
            "11" -> month = "Listopada"
            "12" -> month = "Grudnia"
        }

        val dateFormatted = "$dateDay $month $dateYear | AUTO"
        channelFromDb.forEach { element ->
            if (element.secondsEmpty!! < 24 * 60 * 60) {
                if (element.channelTopic != dateFormatted) {
                    TS3().setTopic(channelId = element.channelId!!, name = dateFormatted)
                }
            }
        }
    }

    fun channelList(typeChannel: String, parentChannelId: Int) {
        val channelList:MutableList<Channel>? = TS3().getCurrentChannelList()
        var sortNumber: Int = 1
        channelList?.forEach { element ->
            if (element.parentChannelId == parentChannelId) {
                val channel: Channels = Channels()
                var id: Long = 0
                if (channelsRepository.existsByChannelId(element.id)) {
                    val channelInDb: Channels = channelsRepository.findByChannelId(element.id)
                    id = channelInDb.id!!
                }
                channel.id = id
                channel.channelBannerURL = element.bannerGraphicsUrl
                channel.channelTopic = element.topic
                channel.parentChannelId = element.parentChannelId
                channel.secondsEmpty = element.secondsEmpty
                channel.sortNumber = sortNumber
                channel.channelName = element.name
                channel.channelId = element.id

                channel.channelType = typeChannel

                if (element.name.contains("Wolny [Otavi.pl]")) {
                    channel.channelStatus = "FREE"
                } else {
                    if (element.maxFamilyClients == 0 && element.maxClients == 0) {
                        channel.channelStatus = "BLOCKED"
                    } else {
                        channel.channelStatus = "OCCUPIED"
                    }
                }

                channelsRepository.save(channel)
                sortNumber++
            }
        }
        val allChannelsDb = channelsRepository.findByChannelType(channelType = typeChannel)
        allChannelsDb.forEach{ element ->
            if (channelList?.find { it.id == element.channelId } == null && element.channelStatus != "DELETED"){
                element.channelStatus = "DELETED"
                element.sortNumber = null
                element.channelBannerURL = null
                element.channelTopic = null
                element.orderId = null
                element.parentChannelId = null
                element.secondsEmpty = null
                element.channelErrorStatus = null
                channelsRepository.save(element)
            }

        }
    }
}