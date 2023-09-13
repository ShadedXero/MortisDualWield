package com.mortisdevelopment.mortisdualwield.dualwield;

import com.mortisdevelopment.mortisdualwield.databases.ItemData;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DualWieldData extends ItemData {

    private final String offHandKey = "MortisDualWieldOffHand";
    private final String weaponKey = "MortisDualWieldWeapon";

    public DualWieldData(@NotNull ItemMeta meta) {
        super(meta);
    }

    public void create(UUID offHand, UUID weapon) {
        setOffHand(offHand);
        setWeapon(weapon);
    }

    public void setOffHand(UUID offHand) {
        if (offHand == null) {
            setString(offHandKey, null);
            return;
        }
        setString(offHandKey, offHand.toString());
    }

    public UUID getOffHand() {
        String value = getString(offHandKey);
        if (value == null) {
            return null;
        }
        return UUID.fromString(value);
    }

    public void setWeapon(UUID weapon) {
        if (weapon == null) {
            setString(weaponKey, null);
            return;
        }
        setString(weaponKey, weapon.toString());
    }

    public UUID getWeapon() {
        String value = getString(weaponKey);
        if (value == null) {
            return null;
        }
        return UUID.fromString(value);
    }
}
