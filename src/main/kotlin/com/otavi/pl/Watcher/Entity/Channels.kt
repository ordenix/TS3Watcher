package com.otavi.pl.Watcher.Entity

import javax.persistence.*


@Entity
@Table(name = "channels")
open class Channels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "channel_id", nullable = false, unique = true)
    open var channelId: Int? = null

    @Column(name = "parent_channel_id")
    open var parentChannelId: Int? = null

    @Column(name = "sort_number")
    open var sortNumber: Int? = null

    @Column(name = "channel_name")
    open var channelName: String? = null

    @Column(name = "channel_topic")
    open var channelTopic: String? = null

    @Column(name = "channel_banner_url")
    open var channelBannerURL: String? = null

    @Column(name = "orderId")
    open var orderId: Int? = null

    @Column(name = "seconds_empty")
    open var secondsEmpty: Int? = null

    @Column(name = "channel_status", nullable = false)
    open var channelStatus: String? = null

    @Column(name = "channel_type", nullable = false)
    open var channelType: String? = null

    @Column(name = "channel_error_status")
    open var channelErrorStatus: String? = null
}