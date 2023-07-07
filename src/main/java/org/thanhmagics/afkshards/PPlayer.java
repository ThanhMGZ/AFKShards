package org.thanhmagics.afkshards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.UUID;

public class PPlayer {

    private UUID uuid;

    private Player player = null;

    private BigDecimal shards;

    private Inventory inventory;

    private Integer i;

    private AFKShop afkShop;

    public PPlayer(UUID uuid) {
        this.uuid = uuid;
        AFKShards.getInstance().getPlayerMap().put(uuid,this);
    }

    public void sendMessage(String... mess) {
        for (String s : mess) {
            player.sendMessage(Utils.applyColor(s));
        }
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public AFKShop getAfkShop() {
        return afkShop;
    }

    public void setAfkShop(AFKShop afkShop) {
        this.afkShop = afkShop;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public BigDecimal getShards() {
        return shards;
    }

    public void setShards(BigDecimal shards) {
        this.shards = shards;
    }

    public void addShards(BigDecimal value) {
        setShards(getShards().add(value));
    }

}
