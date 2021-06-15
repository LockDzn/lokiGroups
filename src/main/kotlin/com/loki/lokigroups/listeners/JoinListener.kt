package com.loki.lokigroups.listeners

import com.loki.lokigroups.LokiGroups
import com.loki.lokigroups.components.GroupObject
import com.loki.lokigroups.components.PlayerObject
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.permissions.PermissionAttachment

class JoinListener: Listener {
    @EventHandler
    fun JoinListener(event: PlayerJoinEvent) {
        val player = event.player

        var groupPlayer = LokiGroups.configManager.getPlayer(player.uniqueId)

        if(groupPlayer == null) {
            val defaultGroup = LokiGroups.configManager.getDefaultGroup()
            val newPlayer = PlayerObject()

            if (defaultGroup is GroupObject) {
                newPlayer.name = player.name
                newPlayer.uuid = player.uniqueId.toString()
                newPlayer.group = defaultGroup.name
            } else {
                newPlayer.name = player.name
                newPlayer.uuid = player.uniqueId.toString()
                newPlayer.group = ""
            }

            LokiGroups.configManager.config.players.add(newPlayer)
            LokiGroups.configManager.saveConfig()
            groupPlayer = newPlayer
        }

        val group = LokiGroups.configManager.getGroup(groupPlayer.group)

        if (group != null) {
            LokiGroups.permissionManager.setPermissions(player.uniqueId, group.permissions)
        }

        LokiGroups.permissionManager.onJoin(player)
        LokiGroups.tagManager.onJoin(player)
    }
}