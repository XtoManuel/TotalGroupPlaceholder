package com.user.totalgroupplaceholder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TotalGroupCommand implements TabExecutor {
    private static final String GROUP_PREFIX = "group.";
    private static final String PLACEHOLDER_PREFIX = "%totalgroup_";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            String version = getClass().getPackage().getImplementationVersion();
            if (version == null) {
                // fallback if not available from manifest
                version = "unknown";
            }
            sender.sendMessage(ChatColor.GREEN + "TotalGroupPlaceholder version: " + version);
            return true;
        }
        LuckPerms luckPerms;
        try {
            luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            sender.sendMessage(ChatColor.RED + "LuckPerms not available");
            return false;
        }

        boolean onlineOnly = false;
        int groupArgIndex = 0;
        if (args.length > 1 && args[args.length - 1].equalsIgnoreCase("--online")) {
            onlineOnly = true;
            groupArgIndex = args.length - 2;
        } else {
            groupArgIndex = args.length - 1;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender, luckPerms);
            return true;
        }

        if (args[0].equalsIgnoreCase("all")) {
            sendAllGroupsCount(sender, luckPerms, onlineOnly);
            return true;
        }

        return sendGroupCount(sender, luckPerms, args[groupArgIndex], onlineOnly);
    }

    private void sendHelpMessage(CommandSender sender, LuckPerms luckPerms) {
        sender.sendMessage(ChatColor.GOLD + "==== TotalGroupPlaceholder ====");
        sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/totalgroup <group|all|help>");
        sender.sendMessage(ChatColor.GRAY + "Required permission: " + ChatColor.WHITE + "totalgroup.use");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "Available placeholders:");
        sender.sendMessage(ChatColor.WHITE + PLACEHOLDER_PREFIX + ChatColor.GRAY + "<group>" + ChatColor.WHITE + "%");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "Current LuckPerms groups:");
        for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + PLACEHOLDER_PREFIX + group.getName().toLowerCase() + "%");
        }
        sender.sendMessage(ChatColor.GOLD + "=============================");
    }

    private void sendAllGroupsCount(CommandSender sender, LuckPerms luckPerms, boolean onlineOnly) {
        List<Group> groups = new ArrayList<>(luckPerms.getGroupManager().getLoadedGroups());
        groups.sort((g1, g2) -> {
            int w1 = g1.getWeight().orElse(0);
            int w2 = g2.getWeight().orElse(0);
            return Integer.compare(w2, w1); // highest weight first
        });
        for (Group group : groups) {
            try {
                String key = GROUP_PREFIX + group.getName().toLowerCase();
                if (!onlineOnly) {
                    int count = luckPerms.getUserManager()
                        .searchAll(net.luckperms.api.node.matcher.NodeMatcher.key(key))
                        .thenApply(java.util.Map::size)
                        .get();
                    sender.sendMessage(group.getName() + ": " + count);
                } else {
                    int count = luckPerms.getUserManager()
                        .searchAll(net.luckperms.api.node.matcher.NodeMatcher.key(key))
                        .thenApply(map -> (int) org.bukkit.Bukkit.getOnlinePlayers().stream().filter(p -> map.containsKey(p.getUniqueId())).count())
                        .get();
                    sender.sendMessage(group.getName() + ": " + count);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                sender.sendMessage(group.getName() + ": Interrupted while searching for members");
            } catch (Exception e) {
                sender.sendMessage(group.getName() + ": Error");
            }
        }
    }

    private boolean sendGroupCount(CommandSender sender, LuckPerms luckPerms, String groupArg, boolean onlineOnly) {
        String groupName = groupArg.toLowerCase();
        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) {
            sender.sendMessage("Group not found");
            return false;
        }
        String key = GROUP_PREFIX + groupName;
        try {
            if (!onlineOnly) {
                int count = luckPerms.getUserManager()
                    .searchAll(net.luckperms.api.node.matcher.NodeMatcher.key(key))
                    .thenApply(java.util.Map::size)
                    .get();
                sender.sendMessage(String.valueOf(count));
            } else {
                int count = luckPerms.getUserManager()
                    .searchAll(net.luckperms.api.node.matcher.NodeMatcher.key(key))
                    .thenApply(map -> (int) org.bukkit.Bukkit.getOnlinePlayers().stream().filter(p -> map.containsKey(p.getUniqueId())).count())
                    .get();
                sender.sendMessage("(online): " + count);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            sender.sendMessage("Interrupted while searching for members");
            return false;
        } catch (Exception e) {
            sender.sendMessage("Error while searching for members");
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final String PLACEHOLDER_ONLINE_SUFFIX = "_online%";
        final String PLACEHOLDER_PLUGIN = "totalgroup";
        LuckPerms luckPerms;
        try {
            luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            return Collections.emptyList();
        }
        List<String> completions = new ArrayList<>();
        String cmdName = command.getName().toLowerCase();

        if (isPapiParseCommand(cmdName, args)) {
            addGroupPlaceholders(completions, luckPerms, PLACEHOLDER_ONLINE_SUFFIX);
        } else if (isPapiPlaceholdersCommand(cmdName, args, PLACEHOLDER_PLUGIN)) {
            addGroupPlaceholders(completions, luckPerms, PLACEHOLDER_ONLINE_SUFFIX);
        } else if (cmdName.equals(PLACEHOLDER_PLUGIN)) {
            // Autocomplete for /totalgroup <group> [--online] and /totalgroup all [--online]
            if (args.length == 1) {
                for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
                    completions.add(group.getName().toLowerCase());
                }
                completions.add("all");
            } else if (args.length == 2) {
                completions.add("--online");
            }
        }
        return completions;
    }

    private boolean isPapiParseCommand(String cmdName, String[] args) {
        return cmdName.equalsIgnoreCase("papi") && args.length >= 2 && args[0].equalsIgnoreCase("parse");
    }

    private boolean isPapiPlaceholdersCommand(String cmdName, String[] args, String pluginName) {
        return cmdName.equalsIgnoreCase("papi") && args.length >= 2 && args[0].equalsIgnoreCase("placeholders") && args[1].equalsIgnoreCase(pluginName);
    }

    private void addGroupPlaceholders(List<String> completions, LuckPerms luckPerms, String onlineSuffix) {
        for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
            String groupName = group.getName().toLowerCase();
            completions.add(PLACEHOLDER_PREFIX + groupName + "%");
            completions.add(PLACEHOLDER_PREFIX + groupName + onlineSuffix);
        }
    }
}
