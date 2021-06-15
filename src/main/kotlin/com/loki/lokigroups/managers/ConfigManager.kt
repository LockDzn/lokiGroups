package com.loki.lokigroups.managers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.loki.lokigroups.LokiGroups
import com.loki.lokigroups.components.ConfigObject
import com.loki.lokigroups.components.GroupObject
import com.loki.lokigroups.components.PlayerObject
import org.bukkit.permissions.PermissionAttachment
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*
import kotlin.collections.HashMap

class ConfigManager {
    private lateinit var configFile: File
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    lateinit var config: ConfigObject

    fun init() {
        loadConfig()
    }

    fun loadConfig() {
        configFile = File(LokiGroups.instance.dataFolder, "config.json")
        if (!configFile.exists()) {
            LokiGroups.instance.dataFolder.mkdir()
            configFile.createNewFile()
            config = ConfigObject()

            val defaultGroup = GroupObject()
            defaultGroup.name = "default"
            defaultGroup.isDefault = true

            config.groups.add(defaultGroup)

            saveConfig()
        }

        config = gson.fromJson(FileReader(configFile), ConfigObject::class.java)
    }

    fun saveConfig() {
        val json = gson.toJson(config)
        configFile.delete()
        Files.write(configFile.toPath(), json.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
    }

    fun getPlayer(uuid: UUID): PlayerObject? {
        var foundPlayer: PlayerObject? = null
        for (player in LokiGroups.configManager.config.players) {
            if (player.uuid == uuid.toString()) {
                foundPlayer = player
            }
        }
        return foundPlayer
    }

    fun getPlayer(name: String): PlayerObject? {
        var foundPlayer: PlayerObject? = null
        for (player in LokiGroups.configManager.config.players) {
            if (player.name.lowercase() == name.lowercase()) {
                foundPlayer = player
            }
        }
        return foundPlayer
    }

    fun getGroup(name: String): GroupObject? {
        var foundGroup: GroupObject? = null
        for (group in LokiGroups.configManager.config.groups) {
            if (group.name.lowercase() == name.lowercase()) {
                foundGroup = group
            }
        }
        return foundGroup
    }

    fun inGroup(uuid: UUID, group: String): Boolean {
        var inGroup = false
        val player = getPlayer(uuid)

        if (player != null && player.group.lowercase() == group.lowercase()) {
            inGroup = true
        }

        return inGroup
    }

    fun inGroup(name: String, group: String): Boolean {
        var inGroup = false
        val player = getPlayer(name)

        if (player != null && player.group.lowercase() == group.lowercase()) {
            inGroup = true
        }

        return inGroup
    }

    fun getDefaultGroup(): GroupObject? {
        var foundGroup: GroupObject? = null
        for (group in LokiGroups.configManager.config.groups) {
            if (group.isDefault) {
                foundGroup = group
            }
        }
        return foundGroup
    }

}