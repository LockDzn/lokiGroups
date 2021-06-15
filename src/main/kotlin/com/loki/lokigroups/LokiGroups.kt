package com.loki.lokigroups

import com.loki.lokigroups.commands.GroupCommand
import com.loki.lokigroups.commands.GroupTabCompleter
import com.loki.lokigroups.listeners.JoinListener
import com.loki.lokigroups.listeners.QuitListener
import com.loki.lokigroups.managers.ConfigManager
import com.loki.lokigroups.managers.PermissionManager
import com.loki.lokigroups.managers.TagManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class LokiGroups: JavaPlugin() {
    companion object {
        lateinit var instance: LokiGroups
        lateinit var configManager: ConfigManager
        lateinit var permissionManager: PermissionManager
        lateinit var tagManager: TagManager
    }

    override fun onEnable() {
        super.onEnable()
        instance = this
        logger.info("Iniciado.")

        configManager = ConfigManager()
        configManager.init()
        permissionManager = PermissionManager()
        tagManager = TagManager()

        registerEvents()
        registerCommand()
    }

    override fun onDisable() {
        super.onDisable()
        logger.info("Até mais.")
    }

    fun registerCommand() {
        getCommand("group")?.setExecutor(GroupCommand())
        getCommand("group")?.setTabCompleter(GroupTabCompleter())
    }

    fun registerEvents() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(JoinListener(), this)
        pm.registerEvents(QuitListener(), this)
    }
}
