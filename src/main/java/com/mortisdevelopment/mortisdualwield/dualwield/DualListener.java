package com.mortisdevelopment.mortisdualwield.dualwield;

import me.deecaad.core.events.EntityEquipmentEvent;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.WeaponMechanicsAPI;
import me.deecaad.weaponmechanics.utils.CustomTag;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponReloadCompleteEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class DualListener implements Listener {

    private final DualManager dualManager;

    public DualListener(DualManager dualManager) {
        this.dualManager = dualManager;
    }

    private void check(@NotNull Player player, @NotNull ItemStack weapon) {
        if (!dualManager.getOnline().contains(player.getUniqueId())) {
            return;
        }
        if (!dualManager.hasPlayer(player.getUniqueId())) {
            return;
        }
        dualManager.removePlayer(player.getUniqueId());
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

    @EventHandler
    public void onEquipment(EntityEquipmentEvent e) {
        if (!e.isEquipping() || !e.getSlot().equals(EquipmentSlot.HAND)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (!dualManager.getOnline().contains(player.getUniqueId())) {
            return;
        }
        ItemStack weapon = e.getEquipped();
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
        dualManager.addPlayer(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        dualManager.getOnline().add(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        dualManager.getOnline().remove(player.getUniqueId());
    }

    @EventHandler
    public void onUnEquip(EntityEquipmentEvent e) {
        System.out.println("UNEQUIP WORKED");
        if (!e.isDequipping() || !e.getSlot().equals(EquipmentSlot.HAND)) {
            return;
        }
        System.out.println("UNEQUIP WORKED 2");
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        System.out.println("UNEQUIP WORKED 3");
        Player player = (Player) e.getEntity();
        check(player, e.getDequipped());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }
        int slot = e.getSlot();
        if (slot == 40) {
            check(player, player.getInventory().getItemInMainHand());
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (!dualManager.getOnline().contains(player.getUniqueId())) {
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
