package org.thanhmagics.afkshards;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "AFKShards";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Thanh Đập Chai";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1.1.1.1.1.1.69";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("amount")) {
            return String.valueOf(AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).getShards().intValue());
        }
        return null;
    }
}
