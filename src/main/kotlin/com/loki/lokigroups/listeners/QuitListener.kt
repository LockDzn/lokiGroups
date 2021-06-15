package com.loki.lokigroups.listeners

import com.loki.lokigroups.LokiGroups
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener : Listener {
    @EventHandler
    fun QuitListener(event: PlayerQuitEvent) {
        val player = event.player
        //LokiGroups.tagManager.onQuit(player)
    }
}