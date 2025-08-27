package com.user.totalgroupplaceholder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TotalGroupCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LuckPerms luckPerms;
        try {
            luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
                sender.sendMessage(ChatColor.RED + "LuckPerms not available");
            return true;
        }


        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.GOLD + "==== TotalGroupPlaceholder ====");
                sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/totalgroup <group|all|help>");
                sender.sendMessage(ChatColor.GRAY + "Required permission: " + ChatColor.WHITE + "totalgroup.use");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.AQUA + "Available placeholders:");
                sender.sendMessage(ChatColor.WHITE + "%totalgroup_" + ChatColor.GRAY + "<group>" + ChatColor.WHITE + "%");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.AQUA + "Current LuckPerms groups:");
            for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + "%totalgroup_" + group.getName().toLowerCase() + "%");
            }
            sender.sendMessage(ChatColor.GOLD + "=============================");
            return true;
        }

        if (args[0].equalsIgnoreCase("all")) {
            for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
                try {
                    int count = luckPerms.getUserManager()
                        .searchAll(net.luckperms.api.node.matcher.NodeMatcher.key("group." + group.getName().toLowerCase()))
                        .thenApply(map -> map.size())
                        .get();
                    sender.sendMessage(group.getName() + ": " + count);
                } catch (Exception e) {
                        sender.sendMessage(group.getName() + ": Error");
                }
            }
            return true;
        }

        String groupName = args[0].toLowerCase();
        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) {
                sender.sendMessage("Group not found");
            return true;
        }
        try {
            int count = luckPerms.getUserManager()
                .searchAll(net.luckperms.api.node.matcher.NodeMatcher.key("group." + groupName))
                .thenApply(map -> map.size())
                .get();
            sender.sendMessage(String.valueOf(count));
        } catch (Exception e) {
                sender.sendMessage("Error while searching for members");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        LuckPerms luckPerms;
        try {
            luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            return Collections.emptyList();
        }
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
                completions.add(group.getName().toLowerCase());
            }
            completions.add("all");
        }
        return completions;
    }
}
