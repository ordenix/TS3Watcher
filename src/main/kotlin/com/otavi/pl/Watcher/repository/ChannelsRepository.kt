package com.otavi.pl.Watcher.repository

import com.otavi.pl.Watcher.Entity.Channels
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChannelsRepository:JpaRepository<Channels, Long> {


    @Query("select c from Channels c where c.channelId = ?1")
    fun findByChannelId(channelId: Int): Channels


    fun existsByChannelId(channelId: Int): Boolean


    @Query("select c from Channels c where c.channelType = ?1")
    fun findByChannelType(channelType: String): List<Channels>


    @Query("select c from Channels c where c.parentChannelId = ?1")
    fun findByParentChannelId(parentChannelId: Int): List<Channels>


}