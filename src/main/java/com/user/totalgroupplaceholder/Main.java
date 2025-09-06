package com.user.totalgroupplaceholder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final String SUPPORTED_PAPI_VERSION = "2.11.6";

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

        // Detect PlaceholderAPI version
        org.bukkit.plugin.Plugin placeholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderApi != null) {
            String version = placeholderApi.getDescription().getVersion();
            if (getLogger().isLoggable(java.util.logging.Level.INFO)) {
                getLogger().info(String.format("PlaceholderAPI version detected: %s", version));
            }
            if (!version.startsWith(SUPPORTED_PAPI_VERSION)) {
                if (getLogger().isLoggable(java.util.logging.Level.SEVERE)) {
                    getLogger().severe(String.format("Unsupported PlaceholderAPI version: %s. This plugin only supports %s. Disabling...", version, SUPPORTED_PAPI_VERSION));
                }
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            new TotalGroupExpansion().register();
            if (getLogger().isLoggable(java.util.logging.Level.INFO)) {
                getLogger().info("PlaceholderAPI expansion registered successfully.");
            }
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
