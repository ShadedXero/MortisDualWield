package com.mortisdevelopment.mortisdualwield.config;

import com.mortisdevelopment.mortisdualwield.dualwield.DualManager;
import com.mortisdevelopment.mortisdualwield.manager.Manager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainConfig extends Config {

    private final Manager manager;

    public MainConfig(Manager manager) {
        super("config.yml");
        this.manager = manager;
        loadConfig();
    }

    @Override
    public void loadConfig() {
        saveConfig();
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        manager.setDualManager(new DualManager(manager.getDataManager()));
        loadWeapons(config);
        manager.getDualManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private void loadWeapons(FileConfiguration config) {
        for (String weaponTitle : config.getStringList("weapons")) {
            manager.getDualManager().getWeaponTitles().add(weaponTitle);
        }
    }

    public Manager getManager() {
        return manager;
    }
}
