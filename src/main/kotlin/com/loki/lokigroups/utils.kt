package com.loki.lokigroups

import org.bukkit.ChatColor

class utils {
    fun formatString(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }
}