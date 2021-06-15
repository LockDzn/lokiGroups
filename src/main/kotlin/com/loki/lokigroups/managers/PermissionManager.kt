package com.loki.lokigroups.managers

import com.loki.lokigroups.LokiGroups
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionAttachment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PermissionManager {
    private val playerAttachment: HashMap<UUID, PermissionAttachment> = HashMap<UUID, PermissionAttachment>()


    fun onJoin(player: Player) {
        val attachment: PermissionAttachment = player.addAttachment(LokiGroups.instance);
        playerAttachment[player.uniqueId] = attachment
    }

    fun onQuit(player: Player) {
        playerAttachment.remove(player.uniqueId)
    }

    fun setPermission(uuid: UUID, permission: String) {
        val attachment = playerAttachment[uuid]
        attachment?.setPermission(permission, true)
    }

    fun setPermissions(uuid: UUID, permissions: ArrayList<String>) {
        val attachment = playerAttachment[uuid]
        for (permission in permissions) {
            attachment?.setPermission(permission, true)
        }
    }

    fun removePermission(uuid: UUID, permission: String) {
        val attachment = playerAttachment[uuid]
        attachment?.unsetPermission(permission)
    }

    fun removePermissions(uuid: UUID, permissions: ArrayList<String>) {
        val attachment = playerAttachment[uuid]
        for (permission in permissions) {
            attachment?.unsetPermission(permission)
        }
    }
}