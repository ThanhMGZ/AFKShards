package org.thanhmagics.afkshards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ShopConfigSave {

    private File file;

    private FileConfiguration config;

    public void init() {
        file = new File(AFKShards.getInstance().getDataFolder(),"ShopConfig.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                AFKShards.getInstance().saveResource("ShopConfig.yml",true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config != null ? config : YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
