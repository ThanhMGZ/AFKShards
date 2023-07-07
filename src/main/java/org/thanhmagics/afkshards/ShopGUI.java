package org.thanhmagics.afkshards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.thanhmagics.afkshards.Utils.getK;

public class ShopGUI {

    private AFKShop afkShop;

    private Player player;

    public ShopGUI(AFKShop afkShop, Player player) {
        this.afkShop = afkShop;
        this.player = player;
    }

    public void open() {
        PPlayer pPlayer = AFKShards.getInstance().getPlayerMap().get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null,afkShop.getSize(),afkShop.getTitle());
        String[] ias = AFKShards.getInstance().getConfig().getString("IAS").split(",");
        int cs = pPlayer.getShards().intValue();
        ItemBuilder e = ItemBuilder.getItemInConfig(AFKShards.getInstance().getConfig(), "ShopItemLoading",new ItemBuilder.LoreReplacer());
        int j = 0;
        List<ItemStack> m = new ArrayList<>();
        for (int i = 0; i < ias.length; i++) {
            if (afkShop.getGuiItems().size() - 1 < i) {
                inventory.setItem(Integer.parseInt(ias[i]), e.build());
                continue;
            }
            String k = (String) getK(afkShop.getGuiItems(),i);
            AFKShop.GUIItem guiItem = afkShop.getGuiItems().get(k);
            if (guiItem.getPrice() <= cs) {
                ItemBuilder clone = new ItemBuilder(guiItem.getItemBuilder().build());
                for (String al : AFKShards.getInstance().getConfig().getStringList("ShopItemLore")) {
                    clone.lore(al.replace("{price}",String.valueOf(guiItem.getPrice())));
                }
                j++;
                //inventory.setItem(Integer.parseInt(ias[i]),clone.build());
                m.add(clone.build());
            } else {
               // inventory.setItem(Integer.parseInt(ias[i]),e.build());
            }
        }
        for (int i = 0; i < ias.length; i++) {
            int slot = Integer.parseInt(ias[i]);
            if ((m.size() - 1) >= i) {
                inventory.setItem(slot,m.get(i));
            } else {
                inventory.setItem(Integer.parseInt(ias[i]),e.build());
            }
        }

        pPlayer.getPlayer().openInventory(inventory);
        pPlayer.setInventory(inventory);
        pPlayer.setI(j);
        pPlayer.setAfkShop(afkShop);
    }

}
