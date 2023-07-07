package org.thanhmagics.afkshards;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.thanhmagics.afkshards.Utils.applyColor;

public class ItemBuilder {
    private ItemStack stack;

    private ItemMeta meta;

    private List<String> lore = new ArrayList<>();

    public ItemBuilder(Material m) {
        this.stack = new ItemStack(m);
        this.meta = stack.getItemMeta();
    }

    public ItemBuilder(String skullValue) {
        this.stack = skullWithValue(skullValue);
        this.meta = stack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.stack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    public ItemBuilder clone() {
        return new ItemBuilder(build().clone());
    }

    public String getDisplayName() {
        return meta.getDisplayName().length() == 0 ? stack.getI18NDisplayName() : meta.getDisplayName();
    }

    public ItemBuilder displayName(String str) {
        if (str != null)
            meta.setDisplayName((applyColor(str)));
        return this;
    }

    public ItemBuilder addDisplayName(String str) {
        if (str != null) {
            if (meta.getDisplayName().length() == 0) {
                meta.setDisplayName(stack.getI18NDisplayName() + Utils.applyColor(str));
                return this;
            }
            meta.setDisplayName(meta.getDisplayName() + Utils.applyColor(str));
        }
        return this;
    }

    public ItemBuilder lore(String str) {
        if (str != null)
            lore.add((applyColor(str)));
        return this;
    }

    public ItemBuilder lore(List<String> strs) {
        for (String s : strs)
            lore(s);
        return this;
    }

    public ItemBuilder enchant(boolean b) {
        if (b) {
            meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                meta.removeEnchant(enchantment);
            }
        }
        return this;
    }

    public ItemStack build() {
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack skullWithValue(String value) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", value));
        meta.setPlayerProfile(profile);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemBuilder getItemInConfig(FileConfiguration config,String path,LoreReplacer loreReplacer) {
        ItemBuilder ib = null;
        if (config.getString(path + ".material").equalsIgnoreCase("PLAYER_HEAD")) {
            if (config.contains(path + ".value")) {
                ib = new ItemBuilder(config.getString(path + ".value"));
            } else {
                ib = new ItemBuilder(Material.PLAYER_HEAD);
            }
        } else {
            ib = new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19");
        }
        if (config.getString(path + ".display") != null) {
            ib.displayName(loreReplacer.apply(config.getString(path + ".display")));
        }
        if (config.getString(path + ".lore") != null) {
            for (String lore : config.getStringList(path + ".lore")) {
                ib.lore(loreReplacer.apply(lore));
            }
        }
        return ib;
    }

    public static class LoreReplacer {

        List<String> t = new ArrayList<>();

        List<String> r = new ArrayList<>();

        public LoreReplacer add(String t,String r) {
            this.t.add(t);
            this.r.add(r);
            return this;
        }

        private String apply(String i) {
            String ni = i;
            for (int j = 0; j < t.size(); j++) {
                if (i.contains(t.get(j))) {
                    ni = i.replace(t.get(j),r.get(j));
                }
            }
            return ni;
        }

    }
}
