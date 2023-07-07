package org.thanhmagics.afkshards;

import org.bukkit.configuration.file.FileConfiguration;

public class AFKReward {

    private String id;

    private String permission;

    private String reward;


    public AFKReward(String id, String permission, String reward) {
        this.id = id;
        this.permission = permission;
        this.reward = reward;
        AFKShards.getInstance().getAfkRewardMap().put(id,this);
    }

    public void saveToConfig() {
        AFKShards.getInstance().getConfig().set("AFKReward." + id + ".permission",permission);
        AFKShards.getInstance().getConfig().set("AFKReward." + id + ".reward",reward);
        AFKShards.getInstance().saveConfig();
    }

    public void delete() {
        AFKShards.getInstance().getAfkRewardMap().remove(id,this);
        AFKShards.getInstance().getConfig().set("AFKReward." + id,null);
        AFKShards.getInstance().saveConfig();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
