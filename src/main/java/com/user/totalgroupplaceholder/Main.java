package com.user.totalgroupplaceholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;

public class Main extends JavaPlugin {

    private static final String TOTALGROUP_COMMAND = "totalgroup";

    @Override
    public void onEnable() {
        getLogger().info("TotalGroupPlaceholder enabled!");

        // Register command and tab completion
        if (getCommand(TOTALGROUP_COMMAND) != null) {
            TotalGroupCommand cmd = new TotalGroupCommand();
            getCommand(TOTALGROUP_COMMAND).setExecutor(cmd);
            getCommand(TOTALGROUP_COMMAND).setTabCompleter(cmd);
        }

        // Detectar versión de PlaceholderAPI
        org.bukkit.plugin.Plugin placeholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderApi != null) {
            String version = placeholderApi.getDescription().getVersion();
            getLogger().info("PlaceholderAPI version detected: " + version);
            if (!version.startsWith("2.11.6")) {
                getLogger().severe("Unsupported PlaceholderAPI version: " + version + ". This plugin only supports 2.11.6. Disabling...");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            new TotalGroupExpansion().register();
            getLogger().info("PlaceholderAPI expansion registered successfully.");
        } else {
            getLogger().warning("PlaceholderAPI is not installed, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("TotalGroupPlaceholder disabled.");
    }

}
