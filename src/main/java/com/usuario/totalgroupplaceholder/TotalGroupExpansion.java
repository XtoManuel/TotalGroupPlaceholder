package com.usuario.totalgroupplaceholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;

public class TotalGroupExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "totalgroup";
    }

    @Override
    public String getAuthor() {
        return "XtoManuel";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        LuckPerms luckPerms;
        try {
            luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            return "LuckPerms no disponible";
        }

        String groupName = params.toLowerCase();
        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) {
            return "Grupo no encontrado";
        }

        try {
            return luckPerms.getUserManager().searchAll(net.luckperms.api.node.matcher.NodeMatcher.key("group." + groupName))
                .thenApply(map -> String.valueOf(map.size()))
                .get();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return "Interrumpido al buscar miembros";
        } catch (Exception e) {
            return "Error al buscar miembros";
        }
    }
}
