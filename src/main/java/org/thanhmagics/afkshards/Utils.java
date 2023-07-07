package org.thanhmagics.afkshards;

import io.netty.channel.*;
import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayInClientCommand;
import net.minecraft.network.protocol.game.PacketPlayOutCommands;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    public static boolean isKTĐB(String s) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9][&][ ]");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

    public static String applyColor(String s) {
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static String unColor(String s) {
        return ChatColor.stripColor(s);
    }

    public List<String> unColor(List<String> strings) {
        List<String> ss = new ArrayList<>();
        for (String s : strings)
            ss.add(unColor(s));
        return ss;
    }

    public static List<String> applyColor(List<String> s) {
        List<String> n = new ArrayList<>();
        for (String ss : s) {
            n.add(applyColor(ss));
        }
        return n;
    }

    public static String[] applyColor(String[] strings) {
        List<String> ss = new ArrayList<>();
        for (String s:
                strings) {
            ss.add(applyColor(s));
        }
        return (String[]) ss.toArray();
    }

    public static int randomNumber(int min,int max) {
        return new Random().nextInt(max - min + 1) + min;
    }



    public static void registerCommands(Plugin plugin, OpenShopCommand commands,String label) {
        PlayerListeners.commandMap.put(label,commands);
       // getCommandMap().register(plugin.getName(), commands);
    }


    public static String locationToString(Location location,boolean f) {
        if (location == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(location.getWorld().getName()).append(";");
        stringBuilder.append(location.getX()).append(";");
        stringBuilder.append(location.getY()).append(";");
        stringBuilder.append(location.getZ()).append(";");
        if (f) {
            stringBuilder.append(location.getYaw()).append(";");
            stringBuilder.append(location.getPitch()).append(";");
        } else {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static Location stringToLocation(String s) {
        if (s == null || s.length() == 0)
            return null;
        return new Location(Bukkit.getWorld(s.split(";")[0]),Double.valueOf(s.split(";")[1]),Double.valueOf(s.split(";")[2]),Double.valueOf(s.split(";")[3]));
    }

    public static ArrayList<Location> getBlocksInArea(Location loc1, Location loc2){
        int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        ArrayList<Location> locs = new ArrayList<Location>();

        for(int x = 0; x<Math.abs(loc1.getBlockX()-loc2.getBlockX()); x++){
            for(int y = 0; y<Math.abs(loc1.getBlockY()-loc2.getBlockY()); y++){
                for(int z = 0; z<Math.abs(loc1.getBlockZ()-loc2.getBlockZ()); z++){
                    locs.add(new Location(loc1.getWorld(),lowX+x, lowY+y, lowZ+z));
                }
            }
        }

        return locs;
    }

    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean isInvFull(Inventory inventory) {
        for (ItemStack itemStack : inventory.getStorageContents()) {
            if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                return false;
            }
        }
        return true;
    }

    public static Object getK(Map<?,?> map, int i) {
        return new ArrayList<>(map.keySet()).get(i);
    }
}
