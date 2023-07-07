package org.thanhmagics.afkshards;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AFKShop {

    private String id,title;

    private int size = 54;

    private String command;

    private String permission;

    private Map<String,GUIItem> guiItems = new HashMap<>();

    public AFKShop(String strings) {
        if (!Utils.isKTĐB(strings) && strings.length() <= 32) {
            this.id = strings;
        } else {
            String[] args = strings.split(";");
            this.title = args[0];
            this.id = args[1];this.size = Integer.valueOf(args[2]);
            this.command = args[3];
            this.permission = args[4];
            if (args.length > 5 && args[5].length() > 0) {
                for (String str : args[5].split("\\|")) {
                    GUIItem guiItem = new GUIItem(new ItemBuilder(ItemData.getItemStack(str.split(",")[1])), null,Integer.valueOf(str.split(",")[3]));
                    guiItems.put(str.split(",")[0],guiItem);
                }
            }
        }
        AFKShards.getInstance().getAfkShopMap().put(id,this);
        if (command != null)
            new OpenShopCommand(getCommand().toLowerCase(),this);
    }

    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(";");
        sb.append(id).append(";").append(size).append(";").append(command).append(";").append(permission);
        if (guiItems.size() > 0) {
            sb.append(";");
            for (String k : guiItems.keySet()) {
                GUIItem guiItem = guiItems.get(k);
                sb.append(k).append(",").append(ItemData.getData(guiItem.getItemBuilder().build())).append(",").append(guiItem.getSlot() != null ? guiItem.getSlot() : "null").append(",").append(guiItem.price).append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        AFKShards.getInstance().getShopConfigSave().getConfig().set("GUI." + id,sb.toString());
        AFKShards.getInstance().getShopConfigSave().save();
    }

    public void delete() {
        AFKShards.getInstance().getAfkShopMap().remove(id);
        AFKShards.getInstance().getShopConfigSave().getConfig().set("GUI." + id,null);
        AFKShards.getInstance().getShopConfigSave().save();
    }


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, GUIItem> getGuiItems() {
        return guiItems;
    }

    public static class GUIItem {

        private ItemBuilder itemBuilder;

        private Integer slot;

        private int price;

        public GUIItem(ItemBuilder itemBuilder, Integer slot, int price) {
            this.itemBuilder = itemBuilder;
            this.slot = slot;
            this.price = price;
        }

        public ItemBuilder getItemBuilder() {
            return itemBuilder;
        }

        public Integer getSlot() {
            return slot;
        }

        public int getPrice() {
            return price;
        }
    }

}
