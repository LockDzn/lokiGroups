package com.loki.lokigroups.commands

import com.google.common.collect.ImmutableList
import com.loki.lokigroups.LokiGroups
import com.loki.lokigroups.commands.subcommands.GroupSubcommands
import com.loki.lokigroups.components.GroupObject
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*

class GroupCommand : CommandExecutor {
    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val groupSubcommands = GroupSubcommands(sender, command, label, args)

        if (args.isEmpty()) {
            sender.sendMessage("§eUtilize /group help para ver os comandos.")
            return true
        }

        when (args[0]) {
            "help" -> {
                return groupSubcommands.help()
            }
            "user" -> {
                return groupSubcommands.user()
            }
            "create" -> {
                return groupSubcommands.createGroup()
            }
            "delete" -> {
                return groupSubcommands.deleteGroup()
            }
            "addpermission" -> {
                return groupSubcommands.addPermission()
            }
            "removepermission" -> {
                return groupSubcommands.removePermission()
            }
            "setprefix" -> {
                return groupSubcommands.setPrefix()
            }
            "setsuffix" -> {
                return groupSubcommands.setSuffix()
            }
            "set" -> {
                return groupSubcommands.setGroup()
            }
        }
        return true
    }

}

class GroupTabCompleter: TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        val completions = mutableListOf<String>()

        if (args[0].isEmpty()) {
            completions.add("help")
            completions.add("user")
            completions.add("create")
            completions.add("addpermission")
            completions.add("removepermission")
            completions.add("setprefix")
            completions.add("setsuffix")
            completions.add("set")

        } else if(args[0] == "addpermission" && args[1].isEmpty()) {
            for (group in LokiGroups.configManager.config.groups) {
                completions.add(group.name)
            }
        } else if(args[0] == "removepermission" && args[1].isEmpty()) {
            for (group in LokiGroups.configManager.config.groups) {
                completions.add(group.name)
            }
        } else if(args[0] == "setprefix" && args[1].isEmpty()) {
            for (group in LokiGroups.configManager.config.groups) {
                completions.add(group.name)
            }
        } else if(args[0] == "setsuffix" && args[1].isEmpty()) {
            for (group in LokiGroups.configManager.config.groups) {
                completions.add(group.name)
            }
        } else if(args[0] == "set" && args[1].isEmpty()) {
            for (group in LokiGroups.configManager.config.groups) {
                completions.add(group.name)
            }
        }

        return completions
    }
}