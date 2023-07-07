package org.thanhmagics.afkshards;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class OpenShopCommand extends Command {


    private String name;

    private AFKShop afkShop;

    public OpenShopCommand(String name, AFKShop afkShop) {
        super(name);
        this.name = name;
        this.afkShop = afkShop;
        init();
    }

    public void init() {
        Utils.registerCommands(AFKShards.getInstance(), this,name);
    }


    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if (s.equalsIgnoreCase(name)) {
            if (afkShop.getPermission() != null) {
                if (!player.hasPermission(afkShop.getPermission())) {
                    player.sendMessage(Utils.applyColor(AFKShards.getInstance().getConfig().getString("Message.PermissionError")));
                    return true;
                }
            }
            if (args.length == 0) {
                new ShopGUI(afkShop,player).open();
            }
        }
        return true;
    }

}
