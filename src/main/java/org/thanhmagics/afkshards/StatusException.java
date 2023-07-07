package org.thanhmagics.afkshards;

import org.bukkit.entity.Player;

public class StatusException extends Exception {

    public StatusException(String message) {
        super(message);
    }

    public void printStackTrace(Player player) {
        player.sendMessage(Utils.applyColor(getMessage()));
    }
}
