package org.thanhmagics.afkshards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.*;

public final class AFKShards extends JavaPlugin {

    private static AFKShards instance;

    private PlayerDataSave playerFiles;

    private ShopConfigSave shopConfigSave;

    private Map<UUID,PPlayer> playerMap = new HashMap<>();

    private Map<String,AFKRegion> afkRegionMap = new HashMap<>();

    private Map<String,AFKReward> afkRewardMap = new HashMap<>();

    private Map<String,AFKShop> afkShopMap = new HashMap<>();



    private Map<Player,AFKRegion> inAfk = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;

        getCommand("AFKShards").setExecutor(new SetupCommand());
        getServer().getPluginManager().registerEvents(new PlayerListeners(),this);
        if (getConfig().getString("MySQL.url").length() == 0)
            getServer().getPluginManager().disablePlugin(this);
        this.playerFiles = new PlayerDataSave();
        this.playerFiles.init(getConfig().getString("MySQL.url"),getConfig().getString("MySQL.username"),getConfig().getString("MySQL.passwords"));
//        this.playerFiles = new PlayerDataSave();
//        this.playerFiles.init(null,null,null);
        for (PPlayer pPlayer : playerFiles.initPlayer()) {
            playerMap.put(pPlayer.getUuid(),pPlayer);
        }
        this.shopConfigSave = new ShopConfigSave() ;
        this.shopConfigSave.init();
        try {
            for (String k : getConfig().getConfigurationSection("AFKReward").getKeys(false)) {
                if (k != null)
                    new AFKReward(k,getConfig().getString("AFKReward." + k + ".permission"),getConfig().getString("AFKReward." + k + ".reward"));
            }
        } catch (Exception e) {}
        try {
            for (String k : getConfig().getConfigurationSection("AFKRegion").getKeys(false)) {
                if (k != null) {
                    AFKRegion afkRegion = new AFKRegion(Utils.stringToLocation(getConfig().getString("AFKRegion." + k + ".pos1")),
                            Utils.stringToLocation(getConfig().getString("AFKRegion." + k + ".pos2")), k);
                    if (getConfig().getString("AFKRegion." + k + ".reward").length() > 0) {
                        String str = getConfig().getString("AFKRegion." + k + ".reward");
                        assert str != null;
                        if (str.contains(",")) {
                            for (String s : str.split(",")) {
                                if (afkRewardMap.containsKey(s)) {
                                    afkRegion.getAr().add(afkRewardMap.get(s));
                                }
                            }
                        } else {
                            if (afkRewardMap.containsKey(str)) {
                                afkRegion.getAr().add(afkRewardMap.get(str));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {}
        try {
            for (String k : getShopConfigSave().getConfig().getConfigurationSection("GUI").getKeys(false)) {
                if (k != null)
                    new AFKShop(getShopConfigSave().getConfig().getString("GUI." + k));
            }
        } catch (Exception e) {}
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            PPlayer pPlayer = playerMap.get(player.getUniqueId());
//            pPlayer.setPlayer(player);
//        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIHook().register();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AFKShards.getInstance().getPlayerMap().containsKey(player.getUniqueId())) {
                PPlayer pPlayer = new PPlayer(player.getUniqueId());
                pPlayer.setPlayer(player);
                pPlayer.setShards(BigDecimal.valueOf(0));
            } else {
                AFKShards.getInstance().getPlayerMap().get(player.getUniqueId()).setPlayer(player);
            }
        }
        AFKChecker.check();
        ActionBarChecker.start();
    }


    public static boolean status = true;
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        status = false;
        this.playerFiles.saveAll();
        this.playerFiles.close();
        for (Player player : Bukkit.getOnlinePlayers()) {
            //((CraftPlayer)player).getHandle().b.disconnect("Server Reload!");
        }
        for (AFKShop afkShop : AFKShards.getInstance().getAfkShopMap().values())
            afkShop.save();
        for (AFKReward afkReward : afkRewardMap.values())
            afkReward.saveToConfig();
        for (AFKRegion afkRegion : afkRegionMap.values())
            afkRegion.saveToConfig();



    }


    public Map<String, AFKShop> getAfkShopMap() {
        return afkShopMap;
    }

    public ShopConfigSave getShopConfigSave() {
        return shopConfigSave;
    }

    public Map<Player,AFKRegion> getInAfk() {
        return inAfk;
    }

    public Map<String, AFKReward> getAfkRewardMap() {
        return afkRewardMap;
    }

    public Map<UUID, PPlayer> getPlayerMap() {
        return playerMap;
    }

    public Map<String, AFKRegion> getAfkRegionMap() {
        return afkRegionMap;
    }

    public PlayerDataSave getPlayerFiles() {
        return playerFiles;
    }

    public static AFKShards getInstance() {
        return instance;
    }
}
