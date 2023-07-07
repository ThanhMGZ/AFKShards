package org.thanhmagics.afkshards;

import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import io.papermc.paper.event.player.PlayerSignCommandPreprocessEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListeners implements Listener {

    private static Map<UUID,String> editor = new HashMap<>();

    public static void addPlayer(UUID p,String code) {
        if (editor.containsKey(p))
            editor.remove(p);
        editor.put(p,code);
    }

    public static void remove(UUID p) {
        editor.remove(p);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
    //    Utils.addPlayer(player);
        if (!AFKShards.getInstance().getPlayerMap().containsKey(player.getUniqueId())) {
            PPlayer pPlayer = new PPlayer(player.getUniqueId());
            pPlayer.setPlayer(player);
            pPlayer.setShards(BigDecimal.valueOf(0));
        } else {
            AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).setPlayer(player);
        }
    }

    public static Map<String, Command> commandMap = new HashMap<>();

    @EventHandler
    public void test(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String command = e.getMessage().split("/")[1].toLowerCase();
        if (commandMap.containsKey(command)) {
            commandMap.get(command).execute(player,command,new String[]{});
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player  = event.getPlayer();
      //  Utils.removePlayer(player);
        AFKShards.getInstance().getInAfk().remove(player);
        AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).setPlayer(null);
        AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).setInventory(null);
        AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).setI(null);
        AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).setAfkShop(null);
    }

    @EventHandler
    public void a(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (editor.containsKey(player.getUniqueId())) {
            String s = editor.get(player.getUniqueId());
            if (s.split("#")[0].equals("1")) {
                event.setCancelled(true);
                addPlayer(player.getUniqueId(),"2#" + s.split("#")[1] + "#" + Utils.locationToString(block.getLocation(),false));
            } else if (s.split("#")[0].equals("2")) {
                event.setCancelled(true);
                String name = s.split("#")[1];
                Location pos1 = Utils.stringToLocation(s.split("#")[2]);
                new AFKRegion(pos1,block.getLocation(),name);
                player.sendMessage("§aCreate Success!");
                remove(player.getUniqueId());
            }
        }
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PPlayer pPlayer = AFKShards.getInstance().getPlayerMap().get(player.getUniqueId());
        if (pPlayer.getInventory() != null) {
            event.setCancelled(true);
            AFKShop afkShop = pPlayer.getAfkShop();
            String[] ias = AFKShards.getInstance().getConfig().getString("IAS").split(",");
            int i = Integer.MAX_VALUE;
            for (int j = 0; j < ias.length; j++) {
                if (Integer.parseInt(ias[j]) == event.getSlot())
                    i = j;
            }
            if (i != Integer.MAX_VALUE) {
                if (pPlayer.getI() > i && pPlayer.getI() != 0) {
                    if (!Utils.isInvFull(player.getInventory())) {
                        String k = (String) Utils.getK(afkShop.getGuiItems(), i);
                        AFKShop.GUIItem guiItem = afkShop.getGuiItems().get(k);
                        int price = guiItem.getPrice();
                        pPlayer.setShards(BigDecimal.valueOf(pPlayer.getShards().intValue() - price));
                        player.getInventory().addItem(guiItem.getItemBuilder().build());
                        for (String s : AFKShards.getInstance().getConfig().getStringList("Message.BuySuccess"))
                            pPlayer.sendMessage(s.replace("{item}",guiItem.getItemBuilder().getDisplayName()));
                    } else {
                        pPlayer.sendMessage(AFKShards.getInstance().getConfig().getString("Message.FullInventory"));
                    }
                }
            }
            new ShopGUI(afkShop,player).open();
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        PPlayer pPlayer = AFKShards.getInstance().getPlayerMap().get(event.getPlayer().getUniqueId());
        if (pPlayer.getInventory() != null) {
            pPlayer.setInventory(null);
            pPlayer.setI(null);
            pPlayer.setAfkShop(null);
        }
    }
}
























