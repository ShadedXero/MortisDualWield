package com.mortisdevelopment.mortisdualwield.dualwield;

import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.WeaponMechanicsAPI;
import me.deecaad.weaponmechanics.utils.CustomTag;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponReloadCompleteEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class DualListener implements Listener {

    private final MortisDualWield plugin = MortisDualWield.getInstance();
    private final DualManager dualManager;

    public DualListener(DualManager dualManager) {
        this.dualManager = dualManager;
    }

    private void unEquipWeapon(@NotNull Player player, @NotNull ItemStack weapon) {
        new BukkitRunnable() {
            @Override
            public void run() {
                dualManager.removeEquiped(player.getUniqueId());
                String weaponTitle = dualManager.getWeaponTitle(weapon);
                if (weaponTitle == null) {
                    return;
                }
                ItemStack dualWield = player.getInventory().getItemInOffHand();
                String dualWieldTitle = dualManager.getWeaponTitle(dualWield);
                if (!weaponTitle.equalsIgnoreCase(dualWieldTitle)) {
                    return;
                }
                dualManager.setWeapon(weapon, dualWield);
                System.out.println(dualManager.getOffHand(weapon));
                player.getInventory().setItemInOffHand(dualManager.getOffHand(weapon));
                dualManager.setOffHand(weapon, new ItemStack(Material.AIR));
            }
        }.runTask(plugin);
    }

    private void unEquipWeapon(@NotNull Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack weapon = player.getInventory().getItemInMainHand();
                dualManager.removeEquiped(player.getUniqueId());
                String weaponTitle = dualManager.getWeaponTitle(weapon);
                if (weaponTitle == null) {
                    return;
                }
                ItemStack dualWield = player.getInventory().getItemInOffHand();
                String dualWieldTitle = dualManager.getWeaponTitle(dualWield);
                if (!weaponTitle.equalsIgnoreCase(dualWieldTitle)) {
                    return;
                }
                dualManager.setWeapon(weapon, dualWield);
                System.out.println(dualManager.getOffHand(weapon));
                player.getInventory().setItemInOffHand(dualManager.getOffHand(weapon));
                dualManager.setOffHand(weapon, new ItemStack(Material.AIR));
            }
        }.runTask(plugin);
    }

    private void equipWeapon(@NotNull Player player, @NotNull ItemStack weapon) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String weaponTitle = dualManager.getWeaponTitle(weapon);
                if (weaponTitle == null || !dualManager.getWeaponTitles().contains(weaponTitle)) {
                    return;
                }
                ItemStack dualWield = dualManager.getWeapon(weapon);
                if (dualWield == null) {
                    dualWield = dualManager.generateWeapon(weaponTitle);
                    dualManager.setWeapon(weapon, dualWield);
                }
                System.out.println(player.getInventory().getItemInOffHand());
                dualManager.setOffHand(weapon, player.getInventory().getItemInOffHand());
                player.getInventory().setItemInOffHand(dualWield);
                dualManager.addEquiped(player.getUniqueId());
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onEquip(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack unEquipped = player.getInventory().getItem(e.getPreviousSlot());
                if (unEquipped != null) {
                    unEquipWeapon(player, unEquipped);
                }
                ItemStack weapon = player.getInventory().getItem(e.getNewSlot());
                if (weapon != null) {
                    equipWeapon(player, weapon);
                }
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }
        if (!dualManager.hasEquiped(player.getUniqueId())) {
            return;
        }
        int slot = e.getSlot();
        if (slot == 40 || slot == player.getInventory().getHeldItemSlot()) {
            e.setCancelled(true);
            unEquipWeapon(player);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (!dualManager.hasEquiped(player.getUniqueId())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onReload(WeaponReloadCompleteEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        boolean mainHand = e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND);
        ItemStack weapon;
        if (mainHand) {
            weapon = player.getInventory().getItemInOffHand();
        }else {
            weapon = player.getInventory().getItemInMainHand();
        }
        String weaponTitle = WeaponMechanicsAPI.getWeaponTitle(weapon);
        if (weaponTitle == null || !weaponTitle.equalsIgnoreCase(e.getWeaponTitle())) {
            return;
        }
        int maxAmmo = WeaponMechanics.getConfigurations().getInt(weaponTitle + ".Reload.Magazine_Size");
        CustomTag.AMMO_LEFT.setInteger(weapon, maxAmmo);
    }
}
