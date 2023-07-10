package com.mortisdevelopment.mortisdualwield;

import com.mortisdevelopment.mortiscorepaper.MortisCorePaper;
import com.mortisdevelopment.mortisdualwield.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MortisDualWield extends JavaPlugin {

    private static MortisDualWield Instance;
    private Manager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        MortisCorePaper.register(this);
        if (getServer().getPluginManager().getPlugin("WeaponMechanics") == null) {
            getServer().getPluginManager().disablePlugin(this);
        }
        manager = new Manager();
    }

    public static MortisDualWield getInstance() {
        return Instance;
    }

    public Manager getManager() {
        return manager;
    }
}
