package com.mortisdevelopment.mortisdualwield.manager;

import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import com.mortisdevelopment.mortisdualwield.config.MainConfig;
import com.mortisdevelopment.mortisdualwield.data.DataManager;
import com.mortisdevelopment.mortisdualwield.dualwield.DualManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class Manager {

    private final MortisDualWield plugin = MortisDualWield.getInstance();
    private DataManager dataManager;
    private DualManager dualManager;
    private MainConfig mainConfig;

    public Manager() {
        this.dataManager = new DataManager();
        this.mainConfig = new MainConfig(this);
        plugin.getCommand("dualwield").setExecutor(new DualWieldCommand(this));
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);
        setDataManager(new DataManager());
        setMainConfig(new MainConfig(this));
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DualManager getDualManager() {
        return dualManager;
    }

    public void setDualManager(DualManager dualManager) {
        this.dualManager = dualManager;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public void setMainConfig(MainConfig mainConfig) {
        this.mainConfig = mainConfig;
    }
}
