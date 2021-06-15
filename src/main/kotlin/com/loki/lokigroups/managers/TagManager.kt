package com.loki.lokigroups.managers

import com.loki.lokigroups.LokiGroups
import com.loki.lokigroups.utils
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TagManager {
    fun onJoin(player: Player) {
        val scoreboard = player.scoreboard
        val playerObject = LokiGroups.configManager.getPlayer(player.uniqueId)

        for (group in LokiGroups.configManager.config.groups) {
            if(scoreboard.getTeam(group.name.lowercase()) == null) {
                val team = scoreboard.registerNewTeam(group.name.lowercase())
                team.prefix = utils().formatString(group.prefix)
                team?.suffix = utils().formatString(group.suffix)
            } else {
                val team = scoreboard.getTeam(group.name.lowercase())
                team?.prefix = utils().formatString(group.prefix)
                team?.suffix = utils().formatString(group.suffix)
            }
        }

        for (target in Bukkit.getOnlinePlayers()) {
            val targetPlayerObject = LokiGroups.configManager.getPlayer(target.uniqueId)
            if (targetPlayerObject != null) {
                val targetTeam = scoreboard.getTeam(targetPlayerObject.group.lowercase())
                targetTeam?.addPlayer(target)
            }
        }

        if (playerObject != null) {
            val groupObject = LokiGroups.configManager.getGroup(playerObject.group)
            if (groupObject != null) {
                setTag(player, playerObject.group)
            }
        }
    }

    fun onQuit(player: Player) {
        val scoreboard = player.scoreboard
        val playerObject = LokiGroups.configManager.getPlayer(player.uniqueId)

        for (group in LokiGroups.configManager.config.groups) {
            val team = scoreboard.getTeam(group.name)
            if (team != null) {
                team.unregister()
            }
        }
    }

    fun setTag(player: Player, group: String) {
        val playerObject = LokiGroups.configManager.getPlayer(player.uniqueId)
        val groupObject = LokiGroups.configManager.getGroup(group)

        if (playerObject != null && groupObject != null) {
            player.setDisplayName(utils().formatString(groupObject.prefix + player.name + groupObject.suffix))
            player.setPlayerListName(utils().formatString(groupObject.prefix + player.name + groupObject.suffix))
            //player.setCustomName(utils().formatString(groupObject.prefix + player.name + groupObject.suffix))

            for (target in Bukkit.getOnlinePlayers()) {
                target.scoreboard.getTeam(groupObject.name.lowercase())?.addPlayer(player)
            }
        }
    }
}