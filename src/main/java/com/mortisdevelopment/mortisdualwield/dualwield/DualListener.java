package com.mortisdevelopment.mortisdualwield.dualwield;

import com.mortisdevelopment.mortisdualwield.MortisDualWield;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.WeaponMechanicsAPI;
import me.deecaad.weaponmechanics.utils.CustomTag;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponPreShootEvent;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponReloadCompleteEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
                dualManager.setOffHand(weapon, player.getInventory().getItemInOffHand());
                player.getInventory().setItemInOffHand(dualWield);
                dualManager.addEquiped(player.getUniqueId());
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
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
        PlayerInventory inv = (PlayerInventory) e.getClickedInventory();
        if (!dualManager.hasEquiped(player.getUniqueId())) {
            return;
        }
        int slot = e.getSlot();
        if (slot == player.getInventory().getHeldItemSlot()) {
            ItemStack weapon = e.getCurrentItem();
            if (weapon == null) {
                return;
            }
            unEquipWeapon(player, weapon);
        }
        if (slot == 40) {
            e.setCancelled(true);
            unEquipWeapon(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack cursor = e.getCursor();
                    if (cursor == null || cursor.getType().isAir()) {
                        player.setItemOnCursor(inv.getItemInOffHand());
                        inv.setItemInOffHand(null);
                    }else {
                        player.setItemOnCursor(inv.getItemInOffHand());
                        inv.setItemInOffHand(cursor);
                    }
                }
            }.runTask(plugin);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (!dualManager.hasEquiped(player.getUniqueId())) {
            return;
        }
        e.setCancelled(true);
        unEquipWeapon(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack weapon = player.getInventory().getItemInMainHand();
                ItemStack offHand = player.getInventory().getItemInOffHand();
                player.getInventory().setItemInMainHand(offHand);
                player.getInventory().setItemInOffHand(weapon);
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onShoot(WeaponPreShootEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (dualManager.hasEquiped(player.getUniqueId()) || !dualManager.getWeaponTitles().contains(e.getWeaponTitle())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onShoot2(WeaponPreShootEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (!dualManager.hasEquiped(player.getUniqueId())) {
            return;
        }
        ItemStack offHand = dualManager.getOffHand(player.getInventory().getItemInMainHand());
        if (offHand == null || offHand.getType().isAir()) {
            return;
        }
        e.setCancelled(true);
        player.sendActionBar(dualManager.getMessage("HANDS_NOT_AVAILABLE"));
    }

    @EventHandler
    public void onShoot3(WeaponPreShootEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (dualManager.hasEquiped(player.getUniqueId()) || !dualManager.getWeaponTitles().contains(e.getWeaponTitle())) {
            return;
        }
        e.setCancelled(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack weapon = player.getInventory().getItemInMainHand();
                if (!weapon.hasItemMeta()) {
                    return;
                }
                equipWeapon(player, weapon);
                int ammo = CustomTag.AMMO_LEFT.getInteger(weapon);
                if (ammo <= 0) {
                    return;
                }
                WeaponMechanicsAPI.shoot(player, e.getWeaponTitle());
                CustomTag.AMMO_LEFT.setInteger(weapon, ammo - 1);
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getAction().isLeftClick()) {
            return;
        }
        Player player = e.getPlayer();
        if (dualManager.hasEquiped(player.getUniqueId())) {
            return;
        }
        ItemStack weapon = e.getItem();
        if (weapon == null) {
            return;
        }
        String weaponTitle = dualManager.getWeaponTitle(weapon);
        if (weaponTitle == null || !dualManager.getWeaponTitles().contains(weaponTitle)) {
            return;
        }
        e.setCancelled(true);
        equipWeapon(player, weapon);
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack offHandWeapon = player.getInventory().getItemInOffHand();
                if (!offHandWeapon.hasItemMeta()) {
                    return;
                }
                int ammo = CustomTag.AMMO_LEFT.getInteger(offHandWeapon);
                if (ammo <= 0) {
                    return;
                }
                WeaponMechanicsAPI.shoot(player, weaponTitle);
                CustomTag.AMMO_LEFT.setInteger(offHandWeapon, ammo - 1);
                //OffHand Weapon does not update its display
            }
        }.runTask(plugin);
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
