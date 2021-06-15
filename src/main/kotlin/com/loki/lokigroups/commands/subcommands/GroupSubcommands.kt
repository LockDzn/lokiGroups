package com.loki.lokigroups.commands.subcommands

import com.loki.lokigroups.LokiGroups
import com.loki.lokigroups.components.GroupObject
import com.loki.lokigroups.utils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GroupSubcommands(val sender: CommandSender, val command: Command, val label: String, val args: Array<out String>) {

    fun help(): Boolean {
        sender.sendMessage("")
        sender.sendMessage("§e§llokiGroups - Comandos")
        sender.sendMessage("")
        sender.sendMessage("§e/group create")
        sender.sendMessage("§e/group delete")
        sender.sendMessage("§e/group user")
        sender.sendMessage("§e/group addpermission")
        sender.sendMessage("§e/group removepermission")
        sender.sendMessage("§e/group setprefix")
        sender.sendMessage("§e/group setsuffix")
        sender.sendMessage("§e/group set")
        sender.sendMessage("")
        return true
    }

    fun user(): Boolean {
        if (args.size < 2) {
            sender.sendMessage("§cUtilize /group user <name>")
            return true
        }

        val findedPlayer = LokiGroups.configManager.getPlayer(args[1])

        if (findedPlayer != null) {
            val findedGroup = LokiGroups.configManager.getGroup(findedPlayer.group)

            if (findedGroup != null) {
                sender.sendMessage("")
                sender.sendMessage("§fUUID: §7${findedPlayer.uuid}")
                sender.sendMessage("§fGrupo: §7${findedGroup.name}")
                sender.sendMessage("§fPermissões: §7${findedGroup.permissions.size}")
                sender.sendMessage("")
            } else {
                sender.sendMessage("")
                sender.sendMessage("§fUUID: §7${findedPlayer.uuid}")
                sender.sendMessage("§fGrupo: §7Nenhum")
                sender.sendMessage("§fpermissões: §70")
                sender.sendMessage("")
            }
        } else {
            sender.sendMessage("§cJogador não encontrado.")
        }

        return true
    }

    fun createGroup(): Boolean {
        if (args.size < 2) {
            sender.sendMessage("§cUtilize /group create <name>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])

        if (findedGroup is GroupObject) {
            sender.sendMessage("§cJá existe um grupo com esse nome.")
            return true
        }

        val newGroup = GroupObject()
        newGroup.name = args[1]
        LokiGroups.configManager.config.groups.add(newGroup)
        LokiGroups.configManager.saveConfig()

        for (target in Bukkit.getOnlinePlayers()) {
            val scoreboard = target.scoreboard
            if (scoreboard.getTeam(newGroup.name.lowercase()) == null) {
                scoreboard.registerNewTeam(newGroup.name.lowercase())
            }
        }

        sender.sendMessage("§aGrupo \"${newGroup.name}\" criado com sucesso!")
        return true
    }

    fun deleteGroup(): Boolean {
        if (args.size < 2) {
            sender.sendMessage("§cUtilize /group delete <group>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])

        if (findedGroup == null) {
            sender.sendMessage("§cEsté grupo não existe.")
            return true
        }

        LokiGroups.configManager.config.groups.remove(findedGroup)
        LokiGroups.configManager.saveConfig()

        for (target in Bukkit.getOnlinePlayers()) {
            val scoreboard = target.scoreboard
            if (scoreboard.getTeam(findedGroup.name.lowercase()) != null) {
                scoreboard.registerNewTeam(findedGroup.name.lowercase())
            }
        }

        sender.sendMessage("§aGrupo \"${findedGroup.name}\" foi deletado com sucesso!")
        return true
    }

    fun addPermission(): Boolean {
        if (args.size < 3) {
            sender.sendMessage("§cUtilize /group addpermission <group> <permission>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])
        val permission = args[2].lowercase()

        if (findedGroup !is GroupObject) {
            sender.sendMessage("§cGrupo não encontrado.")
            return true
        }

        if (findedGroup.permissions.contains(permission)) {
            sender.sendMessage("§cEsse grupo já tem essa permissão.")
            return true
        }

        for (target in Bukkit.getOnlinePlayers()) {
            if (LokiGroups.configManager.inGroup(target.uniqueId, findedGroup.name)) {
                LokiGroups.permissionManager.setPermission(target.uniqueId, permission)
            }
        }

        findedGroup.permissions.add(permission)
        LokiGroups.configManager.saveConfig()
        sender.sendMessage("§aPermissão \"${permission}\" adicionada no grupo ${findedGroup.name}.")
        return true
    }

    fun removePermission(): Boolean {
        if (args.size < 3) {
            sender.sendMessage("§cUtilize /group removepermission <group> <permission>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])
        val permission = args[2].lowercase()

        if (findedGroup !is GroupObject) {
            sender.sendMessage("§cGrupo não encontrado.")
            return true
        }

        if (!findedGroup.permissions.contains(permission)) {
            sender.sendMessage("§cO grupo não tem essa permissão.")
            return true
        }

        for (target in Bukkit.getOnlinePlayers()) {
            if (LokiGroups.configManager.inGroup(target.uniqueId, findedGroup.name)) {
                LokiGroups.permissionManager.removePermission(target.uniqueId, permission)
            }
        }

        findedGroup.permissions.remove(permission)
        LokiGroups.configManager.saveConfig()
        sender.sendMessage("§aPermissão \"${permission}\" foi removida do grupo ${findedGroup.name}.")
        return true
    }

    fun setPrefix(): Boolean {
        if (args.size < 3) {
            sender.sendMessage("§cUtilize /group setprefix <group> <prefix>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])
        val prefix = args[2]

        if (findedGroup !is GroupObject) {
            sender.sendMessage("§cGrupo não encontrado.")
            return true
        }

        findedGroup.prefix = "$prefix "
        LokiGroups.configManager.saveConfig()
        for (target in Bukkit.getOnlinePlayers()) {
            val scoreboard = target.scoreboard
            val team = scoreboard.getTeam(findedGroup.name.lowercase())
            if (team != null) {
                team.prefix = utils().formatString("$prefix ")
            }

            val inGroup = LokiGroups.configManager.inGroup(target.uniqueId, findedGroup.name)
            if (inGroup) {
                LokiGroups.tagManager.setTag(target, findedGroup.name)
            }
        }
        sender.sendMessage("§aO prefixo \"${utils().formatString(prefix)}§r§a\" foi adicionado ao grupo ${findedGroup.name}.")
        return true
    }

    fun setSuffix(): Boolean {
        if (args.size < 3) {
            sender.sendMessage("§cUtilize /group setsuffix <group> <suffix>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])
        val suffix = args[2]

        if (findedGroup !is GroupObject) {
            sender.sendMessage("§cGrupo não encontrado.")
            return true
        }

        findedGroup.suffix = " $suffix"
        LokiGroups.configManager.saveConfig()
        sender.sendMessage("§aO sufixo \"${utils().formatString(suffix)}§r§a\" foi adicionado ao grupo ${findedGroup.name}.")
        return true
    }

    fun setGroup(): Boolean {
        if (args.size < 3) {
            sender.sendMessage("§cUtilize /group set <group> <player>")
            return true
        }

        val findedGroup = LokiGroups.configManager.getGroup(args[1])

        if (findedGroup == null) {
            sender.sendMessage("§cGrupo não encontrado.")
            return true
        }

        val findedPlayerObject = LokiGroups.configManager.getPlayer(args[2])

        if (findedPlayerObject == null) {
            sender.sendMessage("§cJogador não encontrado.")
            return true
        }

        val oldGroup = LokiGroups.configManager.getGroup(findedPlayerObject.group)
        val findedPlayer = Bukkit.getPlayer(args[2])

        if (findedPlayer is Player) {

            if (oldGroup != null) {
                LokiGroups.permissionManager.removePermissions(findedPlayer.uniqueId, oldGroup.permissions)
            }

            LokiGroups.permissionManager.setPermissions(findedPlayer.uniqueId, findedGroup.permissions)
            LokiGroups.tagManager.setTag(findedPlayer, findedGroup.name)
        }

        findedPlayerObject.group = findedGroup.name
        LokiGroups.configManager.saveConfig()
        sender.sendMessage("§aO jogador ${findedPlayerObject.name} foi setado no grupo ${findedGroup.name}.")
        return true
    }
}