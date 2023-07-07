package org.thanhmagics.afkshards;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AFKChecker implements Runnable {

    public static void check() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new AFKChecker());
        executor.shutdown();
    }

    public static Long t = null;

    @Override
    public void run() {
        List<String> aw = new ArrayList<>();
        Map<Chunk,AFKRegion> chunks = new HashMap<>();
        for (AFKRegion ar : AFKShards.getInstance().getAfkRegionMap().values()) {
            aw.add(ar.getPos1().getWorld().getName());
            for (Location l : Utils.getBlocksInArea(ar.getPos1(),ar.getPos2())) {
                if (!chunks.containsKey(l.getChunk())) {
                    chunks.put(l.getChunk(),ar);
                }
            }
        }
        new Timer() {
            int i = 0;
            @Override
            public void run() {
                if (i <= 5) i++; else if (i == 6) { t = System.currentTimeMillis(); i++; }
                if (!AFKShards.status)
                    cancel();
                if (AFKShards.getInstance().getInAfk().size() != 0) {
                    for (Player player : AFKShards.getInstance().getInAfk().keySet()) {
                        String reward = null;
                        if (AFKShards.getInstance().getInAfk().get(player).getAr().size() == 0)
                            continue;
                        for (AFKReward afkReward : AFKShards.getInstance().getAfkRewardMap().values()) {
                            if (!AFKShards.getInstance().getInAfk().get(player).getAr().contains(afkReward))
                                continue;
                            if (player.hasPermission(afkReward.getPermission())) {
                                reward = afkReward.getReward();
                                break;
                            }
                        }
                        if (reward == null)
                            reward = AFKShards.getInstance().getAfkRewardMap().get(AFKShards.getInstance().getConfig().getString("AFKReward_Default")).getReward();
                        if (reward == null) continue;
                        int luckyNumber;
                        if (reward.contains("-")) {
                            int min = Integer.parseInt(reward.split("-")[0]);
                            int max = Integer.parseInt(reward.split("-")[1]);
                            luckyNumber = Utils.randomNumber(min, max);
                        } else {
                            luckyNumber = Integer.parseInt(reward);
                        }
                        AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).addShards(BigDecimal.valueOf(luckyNumber));
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (aw.contains(player.getWorld().getName())) {
                        if (chunks.containsKey(player.getLocation().getChunk())) {
                            AFKRegion afkRegion = chunks.get(player.getLocation().getChunk());
                            if (isInArea(player.getLocation(),afkRegion.getPos1(),afkRegion.getPos2())) {
                                if (!AFKShards.getInstance().getInAfk().containsKey(player))
                                    AFKShards.getInstance().getInAfk().put(player,afkRegion);
                                continue;
                            }
                        }
                    }
                    AFKShards.getInstance().getInAfk().remove(player);
                }
            }
        }.runTask(Integer.parseInt(Objects.requireNonNull(AFKShards.getInstance().getConfig().getString("AFKReward_Phase"))));
    }

    private boolean isInArea(Location l, Location l1, Location l2){
        boolean x = false,y = false,z = false;
        double min = Math.min(l1.getX(),l2.getX());
        double max = Math.max(l1.getX(),l2.getX());
        if (l.getX() <= max && l.getX() >= min)
            x = true;
        min = Math.min(l1.getY(),l2.getY());
        max = Math.max(l1.getY(),l2.getY());
        if (l.getY() <= max && l.getY() >= min)
            y = true;
        min = Math.min(l1.getZ(),l2.getZ());
        max = Math.max(l1.getZ(),l2.getZ());
        if (l.getZ() <= max && l.getZ() >= min)
            z = true;
        return (x && y && z);
    }

    private List<Location> add(List<Location> o, Location n) {
        List<Location> nl = new ArrayList<>(o);
        nl.add(n);
        return nl;
    }
}
