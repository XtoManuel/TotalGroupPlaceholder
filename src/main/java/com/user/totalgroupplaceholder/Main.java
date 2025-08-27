package com.user.totalgroupplaceholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("TotalGroupPlaceholder enabled!");

        // Register command and tab completion
        if (getCommand("totalgroup") != null) {
            TotalGroupCommand cmd = new TotalGroupCommand();
            getCommand("totalgroup").setExecutor(cmd);
            getCommand("totalgroup").setTabCompleter(cmd);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
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
