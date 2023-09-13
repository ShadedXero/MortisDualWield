package com.mortisdevelopment.mortisdualwield.config;

import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import com.mortisdevelopment.mortisdualwield.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public abstract class Config {

    private final MortisDualWield plugin = MortisDualWield.getInstance();
    private final File file;
    private final String fileName;

    public Config(@NotNull String fileName) {
        this.file = new File(plugin.getDataFolder(), fileName);
        this.fileName = fileName;
    }

    public abstract void loadConfig();

    public HashMap<String, Component> loadMessages(ConfigurationSection section) {
        HashMap<String, Component> messageById = new HashMap<>();
        if (section == null) {
            return messageById;
        }
        for (String key : section.getKeys(false)) {
            String id = key.replace("-", "_").toUpperCase();
            String message = section.getString(key);
            messageById.put(id, MessageUtils.color(message));
        }
        return messageById;
    }

    public void saveConfig() {
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }
}
