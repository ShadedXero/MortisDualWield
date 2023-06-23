package com.mortisdevelopment.mortisdualwield.dualwield;


import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import me.deecaad.weaponmechanics.WeaponMechanicsAPI;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DualManager {

    private final MortisDualWield plugin = MortisDualWield.getInstance();
    private final List<String> weaponIds;
    private final HashMap<UUID, DualWield> dualWieldByUUID;

    public DualManager() {
        this.weaponIds = new ArrayList<>();
        this.dualWieldByUUID = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new DualListener(this), plugin);
        test();
    }

    public String getWeaponId(ItemStack item) {
        return WeaponMechanicsAPI.getWeaponTitle(item);
    }

    public List<String> getWeaponIds() {
        return weaponIds;
    }

    public HashMap<UUID, DualWield> getDualWieldByUUID() {
        return dualWieldByUUID;
    }

    public void test() {
        weaponIds.add("Deagle");
    }
}
