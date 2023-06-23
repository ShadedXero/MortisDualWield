package com.mortisdevelopment.mortisdualwield.dualwield;

import org.bukkit.inventory.ItemStack;

public class DualWield {

    private final ItemStack offHand;
    private final ItemStack weapon;
    private final int ammo;

    public DualWield(ItemStack offHand, ItemStack weapon, int ammo) {
        this.offHand = offHand;
        this.weapon = weapon;
        this.ammo = ammo;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public int getAmmo() {
        return ammo;
    }
}
