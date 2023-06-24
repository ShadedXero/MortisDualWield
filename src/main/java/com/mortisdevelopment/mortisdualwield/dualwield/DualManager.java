package com.mortisdevelopment.mortisdualwield.dualwield;

import com.mortisdevelopment.mortiscorepaper.utils.ItemUtils;
import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import com.mortisdevelopment.mortisdualwield.data.DataManager;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.WeaponMechanicsAPI;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DualManager {

    private final DataManager dataManager;
    private final Set<String> weaponTitles;
    private final Set<UUID> online;

    public DualManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.weaponTitles = new HashSet<>();
        this.online = new HashSet<>();
        MortisDualWield plugin = MortisDualWield.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new DualListener(this), plugin);
    }

    public String getWeaponTitle(@NotNull ItemStack item) {
        return WeaponMechanicsAPI.getWeaponTitle(item);
    }

    public ItemStack generateWeapon(@NotNull String weaponId) {
        return WeaponMechanics.getWeaponHandler().getInfoHandler().generateWeapon(weaponId, 1);
    }

    public void create(@NotNull ItemStack dataItem, @NotNull ItemStack offHand, @NotNull ItemStack weapon) {
        if (!dataItem.hasItemMeta()) {
            return;
        }
        DualWieldData data = new DualWieldData(dataItem.getItemMeta());
        UUID oldOffHandUUID = data.getOffHand();
        UUID oldWeaponUUID = data.getWeapon();
        if (oldOffHandUUID != null) {
            dataManager.removeItem(oldOffHandUUID);
        }
        if (oldWeaponUUID != null) {
            dataManager.removeItem(oldWeaponUUID);
        }
        UUID offHandUUID = UUID.randomUUID();
        UUID weaponUUID = UUID.randomUUID();
        data.create(offHandUUID, weaponUUID);
        dataManager.addItem(offHandUUID, ItemUtils.serialize(offHand));
        dataManager.addItem(weaponUUID, ItemUtils.serialize(weapon));
        dataItem.setItemMeta(data.getMeta());
    }

    public void setOffHand(@NotNull ItemStack dataItem, @NotNull ItemStack offHand) {
        if (!dataItem.hasItemMeta()) {
            return;
        }
        DualWieldData data = new DualWieldData(dataItem.getItemMeta());
        UUID oldOffHandUUID = data.getOffHand();
        if (oldOffHandUUID != null) {
            dataManager.removeItem(oldOffHandUUID);
        }
        UUID offHandUUID = UUID.randomUUID();
        data.setOffHand(offHandUUID);
        dataManager.addItem(offHandUUID, ItemUtils.serialize(offHand));
        dataItem.setItemMeta(data.getMeta());
    }

    public void setWeapon(@NotNull ItemStack dataItem, @NotNull ItemStack weapon) {
        if (!dataItem.hasItemMeta()) {
            return;
        }
        DualWieldData data = new DualWieldData(dataItem.getItemMeta());
        UUID oldWeaponUUID = data.getWeapon();
        if (oldWeaponUUID != null) {
            dataManager.removeItem(oldWeaponUUID);
        }
        UUID weaponUUID = UUID.randomUUID();
        data.setWeapon(weaponUUID);
        dataManager.addItem(weaponUUID, ItemUtils.serialize(weapon));
        dataItem.setItemMeta(data.getMeta());
    }

    public ItemStack getOffHand(@NotNull ItemStack dataItem) {
        if (!dataItem.hasItemMeta()) {
            return null;
        }
        DualWieldData data = new DualWieldData(dataItem.getItemMeta());
        UUID offHandUUID = data.getOffHand();
        if (offHandUUID == null) {
            return null;
        }
        String rawItem = dataManager.getItem(offHandUUID);
        if (rawItem == null) {
            return null;
        }
        ItemStack item = ItemUtils.deserialize(rawItem);
        if (item == null) {
            dataManager.removeItem(offHandUUID);
            return null;
        }
        return item;
    }

    public ItemStack getWeapon(@NotNull ItemStack dataItem) {
        if (!dataItem.hasItemMeta()) {
            return null;
        }
        DualWieldData data = new DualWieldData(dataItem.getItemMeta());
        UUID weaponUUID = data.getWeapon();
        if (weaponUUID == null) {
            return null;
        }
        String rawItem = dataManager.getItem(weaponUUID);
        if (rawItem == null) {
            return null;
        }
        ItemStack item = ItemUtils.deserialize(rawItem);
        if (item == null) {
            dataManager.removeItem(weaponUUID);
            return null;
        }
        return item;
    }

    public boolean hasPlayer(@NotNull UUID uuid) {
        return dataManager.hasPlayer(uuid);
    }

    public void addPlayer(@NotNull UUID uuid) {
        dataManager.addPlayer(uuid);
    }

    public void removePlayer(@NotNull UUID uuid) {
        dataManager.removePlayer(uuid);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Set<String> getWeaponTitles() {
        return weaponTitles;
    }

    public Set<UUID> getOnline() {
        return online;
    }
}
