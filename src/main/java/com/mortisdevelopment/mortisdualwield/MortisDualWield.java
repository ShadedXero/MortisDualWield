package com.mortisdevelopment.mortisdualwield;

import com.mortisdevelopment.mortisdualwield.dualwield.DualManager;
import me.none030.mortiscorepaper.MortisCorePaper;
import org.bukkit.plugin.java.JavaPlugin;

public final class MortisDualWield extends JavaPlugin {

    private static MortisDualWield Instance;
    private DualManager dualManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        MortisCorePaper.register(this);
        if (getServer().getPluginManager().getPlugin("WeaponMechanics") == null) {
            getServer().getPluginManager().disablePlugin(this);
        }
        dualManager = new DualManager();
    }

    public static MortisDualWield getInstance() {
        return Instance;
    }

    public DualManager getDualManager() {
        return dualManager;
    }
}
