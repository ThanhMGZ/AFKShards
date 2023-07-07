package org.thanhmagics.afkshards;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AFKRegion {

    private Location pos1,pos2;

    private List<AFKReward> ar = new ArrayList<>();
    private String id;

    public AFKRegion(Location pos1, Location pos2, String id) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.id = id;
        AFKShards.getInstance().getAfkRegionMap().put(id,this);
    }

    public List<AFKReward> getAr() {
        return ar;
    }

    public void saveToConfig() {
        AFKShards.getInstance().getConfig().set("AFKRegion." + id + ".pos1",Utils.locationToString(pos1,false));
        AFKShards.getInstance().getConfig().set("AFKRegion." + id + ".pos2",Utils.locationToString(pos2,false));
        StringBuilder sb = new StringBuilder();
        for (AFKReward afkReward : ar) {
            sb.append(afkReward.getId()).append(",");
        }
        if (ar.size() > 0)
            sb.deleteCharAt(sb.length() - 1);
        AFKShards.getInstance().getConfig().set("AFKRegion." + id + ".reward",sb.toString());
        AFKShards.getInstance().saveConfig();
    }

    public void delete() {
        AFKShards.getInstance().getAfkRegionMap().remove(id,this);
        AFKShards.getInstance().getConfig().set("AFKRegion." + id,null);
        AFKShards.getInstance().saveConfig();
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public String getId() {
        return id;
    }
}
